/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.State;
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

    /** @author qiushaohua 2012-4-4 */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
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
            return null;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("senderId", user.getId());
        params.put("processing", 1);
        params.put("query", query);

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

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OperateResult insert(@RequestBody Order t) {
        // Order order = Converter.mapToBean(map, Order.class);
        // System.err.println(order);
        //
        // OperateResult result = OperateResult.OK;
        //
        // return result;
        String barCode = "";
        String senderIdentityCode = "";
        String receiverIdentityCode = "";
        t.setBarCode(barCode);
        t.setSenderIdentityCode(senderIdentityCode);
        t.setReceiverIdentityCode(receiverIdentityCode);
        t.setState(State.NEW_ORDER.ordinal());
        t.setStateDescribe(State.NEW_ORDER.getDescribe());
        OperateResult insert = super.insert(t);

        if (insert.isOk()) {
            notifyNewOrder(t);
        }

        return insert;
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     */
    private void notifyNewOrder(Order t) {
        sendIdentityToSender(t);
        sendIdentityToReceiver(t);
        sendNewOrderToScheduler(t);
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     */
    private void sendNewOrderToScheduler(Order t) {
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     */
    private void sendIdentityToReceiver(Order t) {
    }

    /**
     * @param t
     * @author qiushaohua 2012-4-5
     */
    private void sendIdentityToSender(Order t) {
    }

    @RequestMapping(value = "/view/{orderId}", method = RequestMethod.GET)
    public String view(@PathVariable int orderId, Map<String, Object> r) {
        r.put("DELIVERED", State.DELIVERED.ordinal());

        Order order = orderService.query(orderId);
        r.put("order", order);

        List<HandleDetail> handleDetail = orderService.getHandleDetail(orderId);
        r.put("handleDetail", handleDetail);

        return "order/view";
    }
}
