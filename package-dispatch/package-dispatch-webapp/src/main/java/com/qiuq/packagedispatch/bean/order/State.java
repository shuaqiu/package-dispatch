/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.packagedispatch.bean.order;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public enum State {

    NEW_ORDER("已下单, 等待调度"),

    SCHEDULED("已调度, 正上门收件"),

    GOT_GOODS("已收件, 正派送"),

    DISPATCHING("派送中"),

    IN_STORAGE("已入库"),

    OUT_STORAGE("已出库"),

    RECEIVED("已送达"),

    CANCELED("已取消");

    private String describe;

    /**
     * 
     */
    private State(String describe) {
        this.describe = describe;
    }

    /** @author qiushaohua 2012-4-4 */
    public String getDescribe() {
        return describe;
    }
}