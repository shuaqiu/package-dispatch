/**
 * @author qiushaohua 2012-4-1
 */
package com.qiuq.packagedispatch.service.customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiuq.packagedispatch.bean.customer.ReceiverCompany;
import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.customer.ReceiverCompanyRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-4-1
 * @version 0.0.1
 */
@Service
public class ReceiverCompanyService extends AbstractResourceService<ReceiverCompany> {

    private ReceiverCompanyRepository receiverCompanyRepository;

    /** @author qiushaohua 2012-4-1 */
    @Autowired
    public void setReceiverCompanyRepository(ReceiverCompanyRepository receiverCompanyRepository) {
        this.receiverCompanyRepository = receiverCompanyRepository;
    }

    @Override
    protected ResourceRepository<ReceiverCompany> getRepository() {
        return receiverCompanyRepository;
    }

}
