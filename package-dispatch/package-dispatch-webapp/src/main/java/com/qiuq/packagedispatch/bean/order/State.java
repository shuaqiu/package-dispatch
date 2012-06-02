/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.packagedispatch.bean.order;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public enum State {

    NEW_ORDER("已下单, 等待调度"), // 0

    SCHEDULED("已调度, 正上门收件"), // 1

    FETCHED("已收件, 正派送"), // 2

    TRANSITING("派送中"), // 3

    IN_STORAGE("已入库"), // 4

    OUT_STORAGE("已出库"), // 5

    DELIVERED("已送达"), // 6

    CANCELED("取消"), // 7

    CLOSED("已关闭");// 8

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