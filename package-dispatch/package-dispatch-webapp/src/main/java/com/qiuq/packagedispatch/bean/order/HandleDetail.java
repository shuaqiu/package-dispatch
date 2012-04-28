/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.packagedispatch.bean.order;

import java.util.Date;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public class HandleDetail extends Detail {

    private Date handleTime;
    private Integer scheduleId;
    private String description;

    /** @author qiushaohua 2012-3-19 */
    public Date getHandleTime() {
        return handleTime;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    /** @author qiushaohua 2012-4-28 */
    public Integer getScheduleId() {
        return scheduleId;
    }

    /** @author qiushaohua 2012-4-28 */
    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getDescription() {
        return description;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setDescription(String desc) {
        description = desc;
    }

}
