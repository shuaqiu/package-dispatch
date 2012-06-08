/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.common.sms.SmsSender;
import com.qiuq.packagedispatch.bean.customer.Receiver;
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.ScheduleDetail;
import com.qiuq.packagedispatch.bean.order.ScheduleHistory;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.customer.ReceiverService;
import com.qiuq.packagedispatch.service.order.OrderService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.HttpSessionUtil;
import com.qiuq.packagedispatch.web.SimpleMessageQueue;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Controller
@RequestMapping("/order")
public class OrderController extends AbstractResourceController<Order> {

    private OrderService orderService;

    private ReceiverService receiverService;

    private SmsSender smsSender;

    private MessageFormat notifyTemplateForSender;
    private MessageFormat notifyTemplateForReceiver;

    private DecimalFormat codeGeneratorFormatter = new DecimalFormat("0000");

    private SimpleMessageQueue<Order> messageQueue;

    /** @author qiushaohua 2012-4-4 */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /** @author qiushaohua 2012-5-14 */
    @Autowired
    public void setReceiverService(ReceiverService receiverService) {
        this.receiverService = receiverService;
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

    /** @author qiushaohua 2012-5-26 */
    @Autowired
    public void setMessageQueue(SimpleMessageQueue<Order> messageQueue) {
        this.messageQueue = messageQueue;
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

        // 2012-5-20 add a state query
        String state = req.getParameter("state");
        if (StringUtils.hasText(state)) {
            params.put("state", State.valueOf(state).ordinal());
        } else {
            // state < State.DELIVERED, means the processing order
            params.put("state", State.DELIVERED.ordinal());
            params.put("stateOp", "<");
        }

        params.put("query", query);

        return doQuery(sort, params, range);
    }

    @Override
    protected OperateResult beforeInsert(Order t) {
        int receiverId = t.getReceiverId();
        if (receiverId <= 0) {
            receiverId = getReceiverId(t);
            if (receiverId <= 0) {
                return new OperateResult(ErrCode.OPERATE_FAIL, "could not insert the receiver");
            }
            t.setReceiverId(receiverId);
        }

        String senderIdentityCode = generateSenderIdentityCode();
        String receiverIdentityCode = generateReceiverIdentityCode(senderIdentityCode);
        t.setSenderIdentityCode(senderIdentityCode);
        t.setReceiverIdentityCode(receiverIdentityCode);
        t.setState(State.NEW_ORDER.ordinal());
        t.setStateDescribe(State.NEW_ORDER.getDescribe());
        return super.beforeInsert(t);
    }

    @Override
    protected OperateResult afterInsert(Order t, OperateResult insertResult) {
        if (insertResult.isOk()) {
            // new Thread(new SmsNotifier(t)).start();
            t.setId(((KeyHolder) insertResult.getObj()).getKey().intValue());
            messageQueue.push(t);
        }
        return super.afterInsert(t, insertResult);
    }

    private int getReceiverId(Order t) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userCompanyId", t.getSenderCompanyId());
        params.put("name", t.getReceiverName());
        params.put("nameOp", "=");
        params.put("company", t.getReceiverCompany());
        params.put("companyOp", "=");

        List<Receiver> companys = receiverService.query("+id", params, null);
        if (companys.size() > 0) {
            return companys.get(0).getId();
        }

        Receiver receiver = new Receiver();
        receiver.setUserCompanyId(t.getSenderCompanyId());
        receiver.setUserCompany(t.getSenderCompany());
        receiver.setName(t.getReceiverName());
        receiver.setTel(t.getReceiverTel());
        receiver.setCompany(t.getReceiverCompany());
        receiver.setAddress(t.getReceiverAddress());

        OperateResult insertResult = receiverService.insert(receiver);
        if (insertResult.isOk()) {
            GeneratedKeyHolder keyHolder = (GeneratedKeyHolder) insertResult.getObj();
            return keyHolder.getKey().intValue();
        }
        return -1;
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
                // 2012-05-24 don't need to send to sender (print it directly)
                // sendIdentityToSender(order);
                // 2012-06-01 don't need to send to receiver (only send when the order is fetched.)
                // sendIdentityToReceiver(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     * @return
     */
    private OperateResult sendIdentityToSender(Order t) {
        String content = notifyTemplateForSender.format(new Object[] {
                t.getSenderIdentityCode(), t.getReceiverName(), t.getReceiverTel(), t.getReceiverCompany(),
                t.getReceiverAddress(), t.getGoodsName(), t.getQuantity()
        });
        return smsSender.send(content, t.getSenderTel());
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     * @return
     */
    private OperateResult sendIdentityToReceiver(Order t) {
        String content = notifyTemplateForReceiver.format(new Object[] {
                t.getReceiverIdentityCode(), t.getSenderName(), t.getSenderTel(), t.getSenderCompany(),
                t.getSenderAddress(), t.getGoodsName(), t.getQuantity(), t.getBarCode()
        });
        return smsSender.send(content, t.getReceiverTel());
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
            OperateResult result = sendIdentityToSender(order);
            return result;
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

            OperateResult result = sendIdentityToReceiver(order);
            return result;
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
            OperateResult result = sendIdentityToReceiver(order);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new OperateResult(ErrCode.OPERATE_FAIL, "fail to resend");
    }

    @RequestMapping(value = "/view/{orderId}", method = RequestMethod.GET)
    public String view(WebRequest req, @PathVariable int orderId, Map<String, Object> r) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user != null) {
            r.put("DELIVERED", State.DELIVERED.ordinal());

            Order order = orderService.query(orderId);
            r.put("order", order);

            List<HandleDetail> handleDetail = orderService.getHandleDetail(orderId);
            r.put("handleDetail", handleDetail);

            if (user.getType() == Type.TYPE_SELF && order.getState() >= State.SCHEDULED.ordinal()) {
                List<ScheduleDetail> scheduleDetail = orderService.getScheduleDetail(orderId);
                r.put("scheduleDetail", scheduleDetail);

                List<ScheduleHistory> scheduleHistory = orderService.getScheduleHistory(orderId);
                r.put("scheduleHistory", scheduleHistory);
            }
        }

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

        // 2012-5-20 add a state query
        String state = req.getParameter("state");
        if (StringUtils.hasText(state)) {
            params.put("state", State.valueOf(state).ordinal());
        } else {
            // state >= State.DELIVERED, means the finished order or canceled order.
            params.put("state", State.DELIVERED.ordinal());
            params.put("stateOp", ">=");
        }

        params.put("query", query);

        return doQuery(sort, params, range);
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-5-10
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public Map<String, Object> print(@RequestParam int orderId) {
        Order t = orderService.query(orderId);
        t.setGoodsName(t.getGoodsName().replaceAll("[\\n|\\r|\\n\\r]", "<br/>"));
        t.setQuantity(t.getQuantity().replaceAll("[\\n|\\r|\\n\\r]", "<br/>"));

        Map<String, Object> rmap = new HashMap<String, Object>();
        rmap.put("order", t);
        return rmap;
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-6-3
     */
    @RequestMapping(value = "/cancel/{orderId}", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult cancel(WebRequest req, @PathVariable int orderId) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new OperateResult(ErrCode.INVALID, "not logined user");
        }

        Order t = new Order();
        t.setId(orderId);
        t.setState(State.CANCELED.ordinal());
        t.setStateDescribe(State.CANCELED.getDescribe());

        HandleDetail detail = createDetail(user, t);
        return orderService.cancelOrClose(t, detail);
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-6-3
     */
    @RequestMapping(value = "/close/{orderId}", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult close(WebRequest req, @PathVariable int orderId, @RequestBody Map<String, Object> params) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new OperateResult(ErrCode.INVALID, "not logined user");
        }

        Order t = new Order();
        t.setId(orderId);
        t.setState(State.CLOSED.ordinal());
        t.setStateDescribe(State.CLOSED.getDescribe() + ": " + Converter.toString(params.get("reason")));

        HandleDetail detail = createDetail(user, t);
        return orderService.cancelOrClose(t, detail);
    }

    /**
     * @param user
     * @param t
     * @return
     * @author qiushaohua 2012-6-3
     */
    private HandleDetail createDetail(User user, Order t) {
        HandleDetail detail = new HandleDetail();
        detail.setOrderId(t.getId());
        detail.setState(t.getState());
        detail.setHandleIndex(1);
        detail.setHandlerId(user.getId());
        detail.setHandlerName(user.getName());
        detail.setHandlerTel(user.getTel());
        detail.setHandleTime(new Date());
        detail.setDescription(t.getStateDescribe());
        return detail;
    }

}
