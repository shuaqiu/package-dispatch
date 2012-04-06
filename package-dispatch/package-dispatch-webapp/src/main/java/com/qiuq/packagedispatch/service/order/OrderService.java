/**
 * @author qiushaohua 2012-4-4
 */
package com.qiuq.packagedispatch.service.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        boolean isUpdated = orderRepository.updateScheduleInfo(orderId, user);

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

        ScheduleDetail fetcher = new ScheduleDetail();
        fetcher.setOrderId(orderId);
        fetcher.setHandlerId(fetcherId);
        fetcher.setHandleIndex(1);
        fetcher.setState(State.FETCHED.ordinal());
        details.add(fetcher);

        int index = 1;
        for (Integer transiterId : transiterIds) {
            ScheduleDetail transiter = new ScheduleDetail();
            fetcher.setOrderId(orderId);
            fetcher.setHandlerId(transiterId);
            fetcher.setHandleIndex(index++);
            fetcher.setState(State.TRANSITING.ordinal());
            details.add(transiter);
        }

        ScheduleDetail deliverer = new ScheduleDetail();
        fetcher.setOrderId(orderId);
        fetcher.setHandlerId(delivererId);
        fetcher.setHandleIndex(1);
        fetcher.setState(State.DELIVERED.ordinal());
        details.add(deliverer);
        return details;
    }

}
