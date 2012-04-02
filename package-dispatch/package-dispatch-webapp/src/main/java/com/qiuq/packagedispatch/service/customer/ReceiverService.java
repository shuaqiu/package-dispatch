/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.service.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuq.packagedispatch.bean.customer.Receiver;
import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.customer.ReceiverRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-3-31
 * @version 0.0.1
 */
@Service
public class ReceiverService extends AbstractResourceService<Receiver> {

    private ReceiverRepository receiveRepository;

    /** @author qiushaohua 2012-3-31 */
    @Autowired
    public void setReceiveRepository(ReceiverRepository receiveRepository) {
        this.receiveRepository = receiveRepository;
    }

    /**
     * @param userId
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-3-22
     */
    @Transactional(readOnly = true)
    public List<Receiver> query(int userId, String sort, String query, long[] range) {
        return receiveRepository.query(userId, sort, query, range);
    }

    @Override
    protected ResourceRepository<Receiver> getRepository() {
        return receiveRepository;
    }

}
