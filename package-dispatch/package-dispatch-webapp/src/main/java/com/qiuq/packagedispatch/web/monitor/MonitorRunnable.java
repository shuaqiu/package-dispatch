/**
 * @author qiushaohua 2012-7-10
 */
package com.qiuq.packagedispatch.web.monitor;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.web.HttpSessionUtil;
import com.qiuq.packagedispatch.web.PermissionHelper;

/**
 * @author qiushaohua 2012-7-10
 * @version 0.0.1
 */
@Component
public class MonitorRunnable implements Runnable {

    private final Log logger = LogFactory.getLog(getClass());

    private final Queue<AsyncContext> contextQueue = new ConcurrentLinkedQueue<AsyncContext>();
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
    private boolean isKeepRunning = true;

    private PermissionHelper permissionHelper;
    private ObjectMapper objectMapper;

    private int pollWaitingSecond = 60;

    /** @author qiushaohua 2012-7-11 */
    @Autowired
    public void setPermissionHelper(PermissionHelper permissionHelper) {
        this.permissionHelper = permissionHelper;
    }

    /** @author qiushaohua 2012-7-11 */
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /** @author qiushaohua 2012-6-26 */
    @Value("${poll.waiting.second}")
    public void setPollWaitingSecond(int pollWaitingSecond) {
        this.pollWaitingSecond = pollWaitingSecond;
    }

    @Override
    public void run() {
        while (isKeepRunning) {
            try {
                doNotify();
            } catch (InterruptedException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * @return
     * @throws InterruptedException
     * @author qiushaohua 2012-7-12
     */
    private void doNotify() throws InterruptedException {
        Message msg = messageQueue.take();
        if (msg == null) {
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("got a message " + msg);
        }
        while (!contextQueue.isEmpty()) {
            AsyncContext context = contextQueue.poll();
            if (context != null) {
                if (isWritable(msg, context)) {
                    writeMessageToContext(msg, context);
                }
                context.complete();
            }
        }
    }

    /**
     * @param context
     * @return
     * @author qiushaohua 2012-7-12
     */
    private boolean isWritable(Message msg, AsyncContext context) {
        ServletWebRequest req = new ServletWebRequest((HttpServletRequest) context.getRequest());
        ServletResponse resp = context.getResponse();

        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            OperateResult result = new OperateResult(ErrCode.NOT_LOGINED, "not logined user");
            writeJsonMessageToResponse(result, resp);
            return false;
        }

        boolean hasPermission = hasPermission(msg, req);
        if (!hasPermission && msg.getMonitorType().isSendErrorIfNotPermit()) {
            OperateResult result = new OperateResult(ErrCode.NOT_PERMISSION, "not permission to monitor "
                    + msg.getMonitorType());
            writeJsonMessageToResponse(result, resp);
            return false;
        }

        msg.setMonitorUser(user);
        msg.setPermit(hasPermission);

        return true;
    }

    /**
     * @param monitorType
     * @param req
     * @return
     * @author qiushaohua 2012-7-17
     */
    private boolean hasPermission(Message msg, WebRequest req) {
        MonitorType monitorType = msg.getMonitorType();
        String permission = monitorType.getPermission();
        if (permission == null) {
            return true;
        }

        // if the notify type special some permission
        Map<String, Boolean> functionMap = HttpSessionUtil.getFunctionMap(req);
        boolean isNotPermit = permissionHelper.isNotPermit(functionMap, permission);
        if (isNotPermit) {
            return false;
        }

        User user = HttpSessionUtil.getLoginedUser(req);
        if (user.getType() == Type.TYPE_SELF) {
            return true;
        }
        return user.getId() == msg.getOrder().getSenderId();
    }

    /**
     * @param msg
     * @param context
     * @author qiushaohua 2012-7-9
     */
    private void writeMessageToContext(Message msg, AsyncContext context) {
        if (logger.isInfoEnabled()) {
            logger.info("prepare to write message to context " + context);
        }

        ServletResponse resp = context.getResponse();
        writeJsonMessageToResponse(msg, resp);

        if (logger.isInfoEnabled()) {
            logger.info("finished to write message to context " + context);
        }
    }

    /**
     * @param msg
     * @param resp
     * @throws IOException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @author qiushaohua 2012-7-11
     */
    void writeJsonMessageToResponse(Object msg, ServletResponse resp) {
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(resp.getOutputStream(),
                    JsonEncoding.UTF8);
            objectMapper.writeValue(jsonGenerator, msg);
            jsonGenerator.flush();
        } catch (JsonGenerationException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        } catch (JsonMappingException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @param context
     * @author qiushaohua 2012-7-10
     */
    public void addContext(AsyncContext context) {
        if (logger.isInfoEnabled()) {
            logger.info("add a context " + context);
        }
        contextQueue.add(context);
        context.setTimeout(pollWaitingSecond * 1000);
    }

    /**
     * @param context
     * @author qiushaohua 2012-7-10
     */
    public void removeContext(AsyncContext context) {
        if (logger.isInfoEnabled()) {
            logger.info("remove context " + context);
        }
        contextQueue.remove(context);
    }

    /**
     * @author qiushaohua 2012-7-10
     */
    public void shutdown() {
        if (logger.isInfoEnabled()) {
            logger.info("shutdown the thread, clear the context");
        }
        isKeepRunning = false;
        contextQueue.clear();
        messageQueue.clear();
    }

    /**
     * @param message
     * @author qiushaohua 2012-7-11
     */
    public void sendMessage(Message message) {
        messageQueue.offer(message);
    }
}