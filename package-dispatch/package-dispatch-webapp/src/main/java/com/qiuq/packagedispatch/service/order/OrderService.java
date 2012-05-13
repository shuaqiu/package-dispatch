/**
 * @author qiushaohua 2012-4-4
 */
package com.qiuq.packagedispatch.service.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.ScheduleDetail;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.order.OrderRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-4-4
 * @version 0.0.1
 */
@Service
public class OrderService extends AbstractResourceService<Order> {

    public OrderRepository orderRepository;

    /** @author qiushaohua 2012-4-4 */
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected ResourceRepository<Order> getRepository() {
        return orderRepository;
    }

    /**
     * @param user
     * @param orderId
     * @param fetcherId
     * @param transiterIds
     * @param delivererId
     * @return
     * @author qiushaohua 2012-4-7
     */
    @Transactional
    public boolean schedule(User user, int orderId, int fetcherId, List<Integer> transiterIds, int delivererId) {
        List<ScheduleDetail> details = buildScheduleDetails(orderId, fetcherId, transiterIds, delivererId);

        boolean isSaved = orderRepository.insertScheduleDetails(details);
        int updated = orderRepository.updateHandlerInfo(orderId);
        boolean isUpdated = orderRepository.updateOrderInfo(orderId, user, fetcherId);

        return isSaved && updated == details.size() && isUpdated;
    }

    /**
     * @param orderId
     * @param fetcherId
     * @param transiterIds
     * @param delivererId
     * @return
     * @author qiushaohua 2012-4-7
     */
    private List<ScheduleDetail> buildScheduleDetails(int orderId, int fetcherId, List<Integer> transiterIds,
            int delivererId) {
        List<ScheduleDetail> details = new ArrayList<ScheduleDetail>(transiterIds.size() + 2);

        details.add(buildScheduleDetail(orderId, fetcherId, 1, State.FETCHED));

        int index = 1;
        for (Integer transiterId : transiterIds) {
            details.add(buildScheduleDetail(orderId, transiterId, index++, State.TRANSITING));
        }

        if (delivererId != -1) {
            details.add(buildScheduleDetail(orderId, delivererId, 1, State.DELIVERED));
        }

        return details;
    }

    /**
     * @param orderId
     * @param handlerId
     * @param handleIndex
     * @param state
     * @return
     * @author qiushaohua 2012-4-7
     */
    private ScheduleDetail buildScheduleDetail(int orderId, int handlerId, int handleIndex, State state) {
        ScheduleDetail detail = new ScheduleDetail();
        detail.setOrderId(orderId);
        detail.setHandlerId(handlerId);
        detail.setHandleIndex(handleIndex);
        detail.setState(state.ordinal());
        return detail;
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-9
     */
    @Transactional(readOnly = true)
    public List<ScheduleDetail> getScheduleDetail(int orderId) {
        return orderRepository.getScheduleDetail(orderId);
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-9
     */
    @Transactional(readOnly = true)
    public List<HandleDetail> getHandleDetail(int orderId) {
        return orderRepository.getHandleDetail(orderId);
    }

    /**
     * @param details
     * @author qiushaohua 2012-4-24
     */
    @Transactional
    public int[] batchHandle(List<HandleDetail> details) {
        return orderRepository.batchHandle(details);
    }

    /**
     * 处理入库/出库
     * 
     * @param user
     * @param barcode
     * @param state
     * @param handler
     * @return
     * @author qiushaohua 2012-4-26
     */
    @Transactional(readOnly = true)
    public OperateResult handleStorage(User user, String barcode, State state, User handler) {
        Order order = orderRepository.getOrder(barcode);
        if (order == null) {
            return new OperateResult(ErrCode.INVALID, "not such barcode");
        }

        boolean isStateMatch = isStateMatch(order, state);
        if (!isStateMatch) {
            return new OperateResult(ErrCode.NOT_CORRECT, "the state of order is not correct");
        }

        try {
            doHandleStorage(user, order, state, handler);
        } catch (Exception e) {
            return new OperateResult(ErrCode.UPDATE_FAIL, "fail to handle storage ");
        }

        return OperateResult.OK;
    }

    /**
     * @param user
     * @param order
     * @param state
     * @param handler
     * @author qiushaohua 2012-4-30
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean doHandleStorage(User user, Order order, State state, User handler) {
        // 创建处理明细, 这里处理人始终为当前用户("值班经理")
        HandleDetail detail = createHandleDetail(order.getId(), user, state);
        boolean inserted = orderRepository.insertHandleDetail(detail);
        if (!inserted) {
            return false;
        }

        User nextCircleHandler = handler == null ? user : handler;
        boolean updated = orderRepository.updateOrderState(order.getId(), nextCircleHandler, state);
        return updated;
    }

    /**
     * @param order
     * @param state
     * @return
     * @author qiushaohua 2012-4-28
     */
    private boolean isStateMatch(Order order, State state) {
        if (state == State.OUT_STORAGE) {
            // if the operation is out of storage, the state of order must be IN_STORAGE
            return order.getState() == State.IN_STORAGE.ordinal();
        }

        if (state == State.IN_STORAGE) {
            // if want to put into storage, the state must be FETCHED or TRANSITING
            return order.getState() == State.FETCHED.ordinal() || order.getState() == State.TRANSITING.ordinal();
        }

        return false;
    }

    /**
     * @param orderId
     * @param user
     * @param state
     * @author qiushaohua 2012-4-26
     */
    private HandleDetail createHandleDetail(int orderId, User user, State state) {
        HandleDetail detail = new HandleDetail();
        detail.setOrderId(orderId);
        detail.setState(state.ordinal());
        detail.setHandleIndex(1);
        detail.setHandlerId(user.getId());
        detail.setHandlerName(user.getName());
        detail.setHandlerTel(user.getTel());
        detail.setHandleTime(new Date());
        detail.setDescription(state.getDescribe());
        return detail;
    }

    /**
     * @param orderId
     * @param receiverIdentityCode
     * @return
     * @author qiushaohua 2012-4-28
     */
    @Transactional
    public boolean updateReceiverIdentiry(int orderId, String receiverIdentityCode) {
        return orderRepository.updateReceiverIdentiry(orderId, receiverIdentityCode);
    }

    /**
     * @return
     * @author qiushaohua 2012-4-28
     */
    @Transactional(readOnly = true)
    public Set<String> getCurrentSenderIdentity() {
        return orderRepository.getCurrentSenderIdentity();
    }

    /**
     * @return
     * @author qiushaohua 2012-4-29
     */
    @Transactional(readOnly = true)
    public List<Order> getNewAlarm() {
        return orderRepository.getNewAlarm();
    }
}
