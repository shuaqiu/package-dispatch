/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.packagedispatch.bean.order;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public class Detail {

    private int id;

    /**
     * 订单信息
     */
    private int orderId;
    /**
     * 状态
     * 
     * @see State
     */
    private int state;
    /**
     * 处理顺序
     */
    private int handleIndex;

    // 处理人信息
    private int handlerId;
    private String handlerName;
    private String handlerTel;

    private String memo;

    /**
     * 
     */
    public Detail() {
        super();
    }

    /** @author qiushaohua 2012-3-19 */
    public int getId() {
        return id;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setId(int id) {
        this.id = id;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getOrderId() {
        return orderId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getState() {
        return state;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setState(int state) {
        this.state = state;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getHandleIndex() {
        return handleIndex;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setHandleIndex(int index) {
        handleIndex = index;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getHandlerId() {
        return handlerId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setHandlerId(int handlerId) {
        this.handlerId = handlerId;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getHandlerName() {
        return handlerName;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getHandlerTel() {
        return handlerTel;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setHandlerTel(String handlerTel) {
        this.handlerTel = handlerTel;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getMemo() {
        return memo;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setMemo(String memo) {
        this.memo = memo;
    }

}