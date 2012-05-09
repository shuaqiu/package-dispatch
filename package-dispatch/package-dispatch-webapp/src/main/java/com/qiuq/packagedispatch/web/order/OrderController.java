/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.sms.SmsSender;
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.ScheduleDetail;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.order.OrderService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Controller
@RequestMapping("/order")
public class OrderController extends AbstractResourceController<Order> {

    private OrderService orderService;

    private SmsSender smsSender;

    private MessageFormat notifyTemplateForSender;
    private MessageFormat notifyTemplateForReceiver;

    private DecimalFormat codeGeneratorFormatter = new DecimalFormat("0000");

    /** @author qiushaohua 2012-4-4 */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /** @author qiushaohua 2012-4-28 */
    @Autowired
    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    /** @author qiushaohua 2012-4-28 */
    @Value("${order.notify.sender}")
    public void setNotifyTemplateForSender(String notifyTemplateForSender) {
        this.notifyTemplateForSender = new MessageFormat(notifyTemplateForSender);
    }

    /** @author qiushaohua 2012-4-28 */
    @Value("${order.notify.receiver}")
    public void setNotifyTemplateForReceiver(String notifyTemplateForReceiver) {
        this.notifyTemplateForReceiver = new MessageFormat(notifyTemplateForReceiver);
    }

    @Override
    protected ResourceService<Order> getService() {
        return orderService;
    }

    /**
     * @param req
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-4-4
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Order>> query(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new HttpEntity<List<Order>>(new ArrayList<Order>());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (user.getType() == Type.TYPE_CUSTOMER) {
            params.put("senderId", user.getId());
        }
        params.put("processing", 1);
        params.put("query", query);

        return doQuery(sort, range, params);
    }

    @Override
    protected OperateResult beforeInsert(Order t) {
        String senderIdentityCode = generateSenderIdentityCode();
        String receiverIdentityCode = generateReceiverIdentityCode(senderIdentityCode);
        t.setSenderIdentityCode(senderIdentityCode);
        t.setReceiverIdentityCode(receiverIdentityCode);
        t.setState(State.NEW_ORDER.ordinal());
        t.setStateDescribe(State.NEW_ORDER.getDescribe());
        return super.beforeInsert(t);
    }

    @Override
    protected OperateResult afterInsert(Order t, boolean isInserted) {
        if (isInserted) {
            new Thread(new SmsNotifier(t)).start();
        }
        return super.afterInsert(t, isInserted);
    }

    /**
     * @return
     * @author qiushaohua 2012-4-28
     */
    private String generateSenderIdentityCode() {
        Set<String> currentSenderIdentity = orderService.getCurrentSenderIdentity();

        while (true) {
            String code = generateIdentityCode();
            if (!currentSenderIdentity.contains(code)) {
                return code;
            }
        }
    }

    /**
     * @param senderIdentityCode
     * @return
     * @author qiushaohua 2012-4-28
     */
    private String generateReceiverIdentityCode(String senderIdentityCode) {
        while (true) {
            String code = generateIdentityCode();
            if (!senderIdentityCode.equals(code)) {
                return code;
            }
        }
    }

    /**
     * @return
     * @author qiushaohua 2012-4-28
     */
    private String generateIdentityCode() {
        int code = (int) (Math.random() * 10000);
        return codeGeneratorFormatter.format(code);
    }

    /**
     * @author qiushaohua 2012-4-28
     * @version 0.0.1
     */
    private class SmsNotifier implements Runnable {
        private Order order;

        public SmsNotifier(Order order) {
            this.order = order;
        }

        @Override
        public void run() {
            try {
                sendIdentityToSender(order);
                sendIdentityToReceiver(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     */
    private void sendIdentityToSender(Order t) {
        String content = notifyTemplateForSender.format(new Object[] {
                t.getSenderIdentityCode(), t.getReceiverName(), t.getReceiverTel(), t.getReceiverCompany(),
                t.getReceiverAddress(), t.getGoodsName(), t.getQuantity()
        });
        smsSender.send(content, t.getSenderTel());
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     */
    private void sendIdentityToReceiver(Order t) {
        String content = notifyTemplateForReceiver.format(new Object[] {
                t.getReceiverIdentityCode(), t.getSenderName(), t.getSenderTel(), t.getSenderCompany(),
                t.getSenderAddress(), t.getGoodsName(), t.getQuantity()
        });
        smsSender.send(content, t.getReceiverTel());
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-28
     */
    @RequestMapping(value = "/{orderId}/identity/sender", method = RequestMethod.GET)
    @ResponseBody
    public OperateResult resendIdentityToSender(@PathVariable int orderId) {
        Order order = query(orderId);
        if (order == null) {
            return new OperateResult(ErrCode.NOT_FOUND, "not such order");
        }
        try {
            sendIdentityToSender(order);
            return OperateResult.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new OperateResult(ErrCode.OPERATE_FAIL, "fail to resend");
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-28
     */
    @RequestMapping(value = "/{orderId}/identity/receiver", method = RequestMethod.GET)
    @ResponseBody
    public OperateResult resendIdentityToReceiver(@PathVariable int orderId) {
        Order order = query(orderId);
        if (order == null) {
            return new OperateResult(ErrCode.NOT_FOUND, "not such order");
        }
        try {
            sendIdentityToReceiver(order);
            return OperateResult.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new OperateResult(ErrCode.OPERATE_FAIL, "fail to resend");
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-28
     */
    @RequestMapping(value = "/{orderId}/identity/receiver", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult regenerateReceiverIdentity(@PathVariable int orderId) {
        Order order = query(orderId);
        if (order == null) {
            return new OperateResult(ErrCode.NOT_FOUND, "not such order");
        }

        String code = generateReceiverIdentityCode(order.getSenderIdentityCode());
        boolean isUpdated = orderService.updateReceiverIdentiry(orderId, code);
        if (!isUpdated) {
            return new OperateResult(ErrCode.UPDATE_FAIL, "fail to update receiver identity");
        }

        order.setReceiverIdentityCode(code);
        try {
            sendIdentityToReceiver(order);
            return OperateResult.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new OperateResult(ErrCode.OPERATE_FAIL, "fail to resend");
    }

    @RequestMapping(value = "/view/{orderId}", method = RequestMethod.GET)
    public String view(@PathVariable int orderId, Map<String, Object> r) {
        r.put("DELIVERED", State.DELIVERED.ordinal());

        Order order = orderService.query(orderId);
        r.put("order", order);

        List<HandleDetail> handleDetail = orderService.getHandleDetail(orderId);
        r.put("handleDetail", handleDetail);
        List<ScheduleDetail> scheduleDetail = orderService.getScheduleDetail(orderId);
        r.put("scheduleDetail", scheduleDetail);

        return "order/view";
    }

    /**
     * @return
     * @author qiushaohua 2012-5-10
     */
    @RequestMapping(value = "/history/list", method = RequestMethod.GET)
    public String historylist() {
        return "order/history";
    }

    /**
     * @param req
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-5-10
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public HttpEntity<List<Order>> history(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new HttpEntity<List<Order>>(new ArrayList<Order>());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (user.getType() == Type.TYPE_CUSTOMER) {
            params.put("senderId", user.getId());
        }
        params.put("state", State.DELIVERED.ordinal());
        params.put("query", query);

        return doQuery(sort, range, params);
    }

    /**
     * @param sort
     * @param range
     * @param params
     * @return
     * @author qiushaohua 2012-5-10
     */
    private HttpEntity<List<Order>> doQuery(String sort, String range, Map<String, Object> params) {
        long[] rangeArr = range(range);

        HttpHeaders header = new HttpHeaders();
        if (rangeArr != null) {
            long count = orderService.matchedRecordCount(params);
            header.set("Content-Range", " items " + (rangeArr[0] - 1) + "-" + (rangeArr[1] - 1) + "/" + count);
        }

        List<Order> list = orderService.query(sort, params, range(range));
        HttpEntity<List<Order>> entity = new HttpEntity<List<Order>>(list, header);

        return entity;
    }
}
