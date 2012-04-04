/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.bean.order;

import java.util.Date;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
public class Order {

    private int id;

    // 寄件人信息
    private int senderId;
    private String senderName;
    private String senderTel;
    private String senderCompany;
    private String senderAddress;

    // 收件人信息
    private int receiverId;
    private String receiverName;
    private String receiverTel;
    private String receiverCompany;
    private String receiverAddress;

    // 单信息
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 物品名称
     */
    private String goodsName;
    /**
     * 数量
     */
    private String quantity;

    /**
     * 条形码
     */
    private String barCode;
    /**
     * 寄件人识别码
     */
    private String senderIdentityCode;
    /**
     * 收件人识别码
     */
    private String receiverIdentityCode;

    /**
     * 从寄件人拿到货物的时间
     */
    private Date startTime;
    /**
     * 将货物送到收件人的时间
     */
    private Date endTime;

    // 调度员信息
    private Integer schedulerId;
    private String schedulerName;
    private String schedulerTel;
    private Date scheduleTime;

    /**
     * 订单状态
     * 
     * @see State
     */
    private int state;

    /** @author qiushaohua 2012-3-19 */
    public int getId() {
        return id;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setId(int id) {
        this.id = id;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getSenderId() {
        return senderId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getSenderName() {
        return senderName;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getSenderTel() {
        return senderTel;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getSenderCompany() {
        return senderCompany;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setSenderCompany(String senderCompany) {
        this.senderCompany = senderCompany;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getSenderAddress() {
        return senderAddress;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getReceiverId() {
        return receiverId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getReceiverName() {
        return receiverName;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getReceiverTel() {
        return receiverTel;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getReceiverCompany() {
        return receiverCompany;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setReceiverCompany(String receiverCompany) {
        this.receiverCompany = receiverCompany;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    /** @author qiushaohua 2012-3-19 */
    public Date getOrderTime() {
        return orderTime;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getGoodsName() {
        return goodsName;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getQuantity() {
        return quantity;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * 条形码
     * 
     * @author qiushaohua 2012-3-19
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * 条形码
     * 
     * @author qiushaohua 2012-3-19
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * 寄件人识别码
     * 
     * @author qiushaohua 2012-3-19
     */
    public String getSenderIdentityCode() {
        return senderIdentityCode;
    }

    /**
     * 寄件人识别码
     * 
     * @author qiushaohua 2012-3-19
     */
    public void setSenderIdentityCode(String senderIdentityCode) {
        this.senderIdentityCode = senderIdentityCode;
    }

    /**
     * 收件人识别码
     * 
     * @author qiushaohua 2012-3-19
     */
    public String getReceiverIdentityCode() {
        return receiverIdentityCode;
    }

    /**
     * 收件人识别码
     * 
     * @author qiushaohua 2012-3-19
     */
    public void setReceiverIdentityCode(String receiverIdentityCode) {
        this.receiverIdentityCode = receiverIdentityCode;
    }

    /**
     * 从寄件人拿到货物的时间
     * 
     * @author qiushaohua 2012-3-19
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 从寄件人拿到货物的时间
     * 
     * @author qiushaohua 2012-3-19
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 将货物送到收件人的时间
     * 
     * @author qiushaohua 2012-3-19
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 将货物送到收件人的时间
     * 
     * @author qiushaohua 2012-3-19
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /** @author qiushaohua 2012-3-19 */
    public Integer getSchedulerId() {
        return schedulerId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setSchedulerId(Integer schedulerId) {
        this.schedulerId = schedulerId;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getSchedulerName() {
        return schedulerName;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    /** @author qiushaohua 2012-3-23 */
    public String getSchedulerTel() {
        return schedulerTel;
    }

    /** @author qiushaohua 2012-3-23 */
    public void setSchedulerTel(String schedulerTel) {
        this.schedulerTel = schedulerTel;
    }

    /** @author qiushaohua 2012-3-19 */
    public Date getScheduleTime() {
        return scheduleTime;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    /**
     * 订单状态
     * 
     * @see State
     * @author qiushaohua 2012-3-19
     */
    public int getState() {
        return state;
    }

    /**
     * 订单状态
     * 
     * @see State
     * @author qiushaohua 2012-3-19
     */
    public void setState(int state) {
        this.state = state;
    }

}
