/**
 * @author qiushaohua 2012-6-8
 */
package com.qiuq.packagedispatch.bean.order;

import java.util.Date;

/**
 * @author qiushaohua 2012-6-8
 * @version 0.0.1
 */
public class ScheduleHistory {

    private int id;
    private int orderId;

    private String schedulerName;
    private String schedulerTel;
    private Date scheduleTime;

    /** @author qiushaohua 2012-6-8 */
    public int getId() {
        return id;
    }

    /** @author qiushaohua 2012-6-8 */
    public void setId(int id) {
        this.id = id;
    }

    /** @author qiushaohua 2012-6-8 */
    public int getOrderId() {
        return orderId;
    }

    /** @author qiushaohua 2012-6-8 */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /** @author qiushaohua 2012-6-8 */
    public String getSchedulerName() {
        return schedulerName;
    }

    /** @author qiushaohua 2012-6-8 */
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    /** @author qiushaohua 2012-6-8 */
    public String getSchedulerTel() {
        return schedulerTel;
    }

    /** @author qiushaohua 2012-6-8 */
    public void setSchedulerTel(String schedulerTel) {
        this.schedulerTel = schedulerTel;
    }

    /** @author qiushaohua 2012-6-8 */
    public Date getScheduleTime() {
        return scheduleTime;
    }

    /** @author qiushaohua 2012-6-8 */
    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

}
