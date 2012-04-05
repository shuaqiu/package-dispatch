/**
 * @author qiushaohua 2012-4-4
 */
package com.qiuq.packagedispatch.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiuq.packagedispatch.bean.order.Order;
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
    public List<Order> query(String sort, Map<String, Object> params, long[] range) {
        return orderRepository.query(sort, params, range);
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    public long matchedRecordCount(Map<String, Object> params) {
        return orderRepository.matchedRecordCount(params);
    }

}
