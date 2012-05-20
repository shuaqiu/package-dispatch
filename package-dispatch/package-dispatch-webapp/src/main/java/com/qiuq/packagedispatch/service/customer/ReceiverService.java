/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResourceRepository<Receiver> getRepository() {
        return receiveRepository;
    }

}
