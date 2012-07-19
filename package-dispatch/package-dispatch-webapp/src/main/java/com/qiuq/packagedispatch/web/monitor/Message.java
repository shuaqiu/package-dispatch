/**
 * @author qiushaohua 2012-7-15
 */
package com.qiuq.packagedispatch.web.monitor;

import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.system.User;

/**
 * @author qiushaohua 2012-7-15
 * @version 0.0.1
 */
public class Message {

    /**
     * just for the JS to run
     */
    private final boolean isOk = true;
    private Order order;
    private Object detail;

    private MonitorType monitorType;
    private User monitorUser;
    private boolean isPermit;

    /**
     * @param obj
     */
    public Message(Order order) {
        this.order = order;
    }

    /**
     * @param obj
     * @param detail
     */
    public Message(Order order, Object detail) {
        this.order = order;
        this.detail = detail;
    }

    /** @author qiushaohua 2012-7-19 */
    public boolean isOk() {
        return isOk;
    }

    /** @author qiushaohua 2012-7-19 */
    public Order getOrder() {
        return order;
    }

    /** @author qiushaohua 2012-7-19 */
    public Object getDetail() {
        return detail;
    }

    /** @author qiushaohua 2012-7-15 */
    public MonitorType getMonitorType() {
        return monitorType;
    }

    /** @author qiushaohua 2012-7-15 */
    public void setMonitorType(MonitorType monitorType) {
        this.monitorType = monitorType;
    }

    /** @author qiushaohua 2012-7-15 */
    public User getMonitorUser() {
        return monitorUser;
    }

    /** @author qiushaohua 2012-7-15 */
    public void setMonitorUser(User monitorUser) {
        this.monitorUser = monitorUser;
    }

    /** @author qiushaohua 2012-7-18 */
    public boolean isPermit() {
        return isPermit;
    }

    /** @author qiushaohua 2012-7-18 */
    public void setPermit(boolean isPermit) {
        this.isPermit = isPermit;
    }
}
