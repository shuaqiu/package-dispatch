/**
 * @author qiushaohua 2012-4-4
 */
package com.qiuq.packagedispatch.service.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private OrderRepository orderRepository;

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
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Transactional(readOnly = true)
    public List<Order> query(String sort, Map<String, Object> params, long[] range) {
        return orderRepository.query(sort, params, range);
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Transactional(readOnly = true)
    public long matchedRecordCount(Map<String, Object> params) {
        return orderRepository.matchedRecordCount(params);
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

        details.add(buildScheduleDetail(orderId, delivererId, 1, State.DELIVERED));

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
     * @param user
     * @param barcode
     * @param state
     * @return
     * @author qiushaohua 2012-4-26
     */
    @Transactional
    public OperateResult handleStorage(User user, String barcode, State state) {
        int orderId = orderRepository.getOrderId(barcode);
        if (orderId == 0) {
            return new OperateResult(ErrCode.INVALID, "not such barcode");
        }

        HandleDetail detail = createHandleDetail(orderId, user, state);
        boolean inserted = orderRepository.insertHandleDetail(detail);
        if (!inserted) {
            return new OperateResult(ErrCode.INSERT_FAIL, "fail to insert handle detail");
        }

        boolean updated = orderRepository.updateOrderState(orderId, user, state);
        if (!updated) {
            return new OperateResult(ErrCode.UPDATE_FAIL, "fail to update order state");
        }

        return OperateResult.OK;
    }

    /**
     * @param orderId
     * @param user
     * @param state
     * @author qiushaohua 2012-4-26
     */
    protected HandleDetail createHandleDetail(int orderId, User user, State state) {
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

}
