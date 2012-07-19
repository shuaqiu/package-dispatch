/**
 * @author qiushaohua 2012-6-25
 */
package com.qiuq.packagedispatch.web.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.service.order.OrderService;

/**
 * @author qiushaohua 2012-6-25
 * @version 0.0.1
 */
@Controller
@RequestMapping("/notify")
public class NotifyController {

    private MonitorRunnable monitorRunnable;

    private OrderService orderService;

    /** @author qiushaohua 2012-7-13 */
    @Autowired
    public void setMonitorRunnable(MonitorRunnable notifier) {
        monitorRunnable = notifier;
    }

    /** @author qiushaohua 2012-7-17 */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * @param type
     * @param map
     * @return
     * @author qiushaohua 2012-6-26
     */
    @RequestMapping(value = "/handle/{barcode}", method = RequestMethod.POST)
    @ResponseBody
    public OperateResult handle(@PathVariable String barcode) {
        try {
            Order order = orderService.getOrder(barcode);
            List<HandleDetail> handleDetail = orderService.getHandleDetail(order.getId());
            return fire(MonitorType.HANDLE_ORDER, order, handleDetail.get(handleDetail.size() - 1));
        } catch (Exception e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.INVALID, "special a invalid type");
        }
    }

    /**
     * @param barcode
     * @param detail
     * @return
     * @author qiushaohua 2012-7-18
     */
    public OperateResult handle(String barcode, HandleDetail detail) {
        try {
            Order order = orderService.getOrder(barcode);
            return fire(MonitorType.HANDLE_ORDER, order, detail);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.INVALID, "special a invalid type");
        }
    }

    /**
     * @param orderId
     * @param detail
     * @return
     * @author qiushaohua 2012-7-18
     */
    public OperateResult handle(int orderId, HandleDetail detail) {
        try {
            Order order = orderService.query(orderId);
            return fire(MonitorType.HANDLE_ORDER, order, detail);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.INVALID, "special a invalid type");
        }
    }

    /**
     * @param type
     * @param order
     * @return
     * @author qiushaohua 2012-7-15
     */
    public OperateResult fire(MonitorType type, Order order) {
        Message message = new Message(order);
        message.setMonitorType(type);
        monitorRunnable.sendMessage(message);
        return OperateResult.OK;
    }

    /**
     * @param type
     * @param order
     * @param detail
     * @return
     * @author qiushaohua 2012-7-15
     */
    public OperateResult fire(MonitorType type, Order order, Object detail) {
        Message message = new Message(order, detail);
        message.setMonitorType(type);
        monitorRunnable.sendMessage(message);
        return OperateResult.OK;
    }
}
