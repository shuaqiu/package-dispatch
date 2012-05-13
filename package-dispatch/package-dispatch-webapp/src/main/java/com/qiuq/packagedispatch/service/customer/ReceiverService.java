/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.service.customer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.customer.Receiver;
import com.qiuq.packagedispatch.bean.customer.ReceiverCompany;
import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.customer.ReceiverCompanyRepository;
import com.qiuq.packagedispatch.repository.customer.ReceiverRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-3-31
 * @version 0.0.1
 */
@Service
public class ReceiverService extends AbstractResourceService<Receiver> {

    private ReceiverRepository receiveRepository;

    private ReceiverCompanyRepository receiverCompanyRepository;

    /** @author qiushaohua 2012-3-31 */
    @Autowired
    public void setReceiveRepository(ReceiverRepository receiveRepository) {
        this.receiveRepository = receiveRepository;
    }

    /** @author qiushaohua 2012-5-14 */
    @Autowired
    public void setReceiverCompanyRepository(ReceiverCompanyRepository receiverCompanyRepository) {
        this.receiverCompanyRepository = receiverCompanyRepository;
    }

    @Override
    public ResourceRepository<Receiver> getRepository() {
        return receiveRepository;
    }

    @Override
    protected OperateResult beforeInsert(Receiver t) {
        int companyId = Converter.toInt(t.getCompanyId());
        if (companyId <= 0) {
            companyId = getCompanyId(t);
            if (companyId <= 0) {
                return new OperateResult(ErrCode.OPERATE_FAIL, "could not insert the company");
            }
            t.setCompanyId(companyId);
        }

        return super.beforeInsert(t);
    }

    @Override
    protected OperateResult beforeUpdate(Receiver t) {
        int companyId = Converter.toInt(t.getCompanyId());
        if (companyId <= 0) {
            companyId = getCompanyId(t);
            if (companyId <= 0) {
                return new OperateResult(ErrCode.OPERATE_FAIL, "could not insert the company");
            }
            t.setCompanyId(companyId);
        }

        return super.beforeUpdate(t);
    }

    /**
     * @param t
     * @return
     * @author qiushaohua 2012-5-13
     */
    private int getCompanyId(Receiver t) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", t.getUserId());
        params.put("name", t.getCompany());
        params.put("nameOp", "=");
        List<ReceiverCompany> companys = receiverCompanyRepository.query("+id", params, null);
        if (companys.size() > 0) {
            return companys.get(0).getId();
        }

        ReceiverCompany company = new ReceiverCompany();
        company.setUserId(t.getUserId());
        company.setName(t.getCompany());
        company.setAddress(t.getAddress());

        OperateResult insertResult = receiverCompanyRepository.insert(company);
        if (insertResult.isOk()) {
            GeneratedKeyHolder keyHolder = (GeneratedKeyHolder) insertResult.getObj();
            return keyHolder.getKey().intValue();
        }
        return -1;
    }

}
