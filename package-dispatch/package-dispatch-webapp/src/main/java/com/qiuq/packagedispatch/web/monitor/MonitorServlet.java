/**
 * @author qiushaohua 2012-7-8
 */
package com.qiuq.packagedispatch.web.monitor;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-7-8
 * @version 0.0.1
 */
@WebServlet(urlPatterns = "/notify", asyncSupported = true)
public class MonitorServlet extends HttpServlet {
    private static final long serialVersionUID = 1982451095263479461L;

    private final Log logger = LogFactory.getLog(getClass());

    private MonitorRunnable notifier;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        XmlWebApplicationContext applicationContext = (XmlWebApplicationContext) context
                .getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.web");
        if (applicationContext != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("get notifier from application context");
            }
            notifier = applicationContext.getBean(MonitorRunnable.class);

            if (notifier != null) {
                if (logger.isInfoEnabled()) {
                    logger.info("start the notifier thread");
                }
                new Thread(notifier, "Notifier Thread").start();
            }
        }

    }

    @Override
    public void destroy() {
        if (logger.isInfoEnabled()) {
            logger.info("try to destroy the notifier servlet");
        }
        if (notifier != null) {
            notifier.shutdown();
        }
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        ServletWebRequest webreq = new ServletWebRequest(req);
        User user = HttpSessionUtil.getLoginedUser(webreq);
        if (user == null) {
            OperateResult msg = new OperateResult(ErrCode.NOT_LOGINED, "not logined user");
            notifier.writeJsonMessageToResponse(msg, resp);
            return;
        }

        final AsyncContext context = req.startAsync();
        context.addListener(new AsyncListener() {

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                if (logger.isDebugEnabled()) {
                    logger.debug("request start");
                }
            }

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                if (logger.isDebugEnabled()) {
                    logger.debug("request completed");
                }
                notifier.removeContext(context);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                if (logger.isDebugEnabled()) {
                    logger.debug("request occur error");
                }
                notifier.removeContext(context);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                if (logger.isDebugEnabled()) {
                    logger.debug("request time out");
                }
                OperateResult msg = new OperateResult(ErrCode.NULL, "null");
                notifier.writeJsonMessageToResponse(msg, resp);
                notifier.removeContext(context);
            }
        });
        notifier.addContext(context);
    }
}
