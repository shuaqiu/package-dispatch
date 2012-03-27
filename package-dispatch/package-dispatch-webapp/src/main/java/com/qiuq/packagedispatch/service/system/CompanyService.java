/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.system.CompanyRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Service
public class CompanyService extends AbstractResourceService<Company> {

    private CompanyRepository companyRepository;

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * @return
     * @author qiushaohua 2012-3-24
     * @param sort
     * @param query
     */
    @Transactional(readOnly = true)
    public List<Company> query(String sort, String query) {
        return companyRepository.query(sort, query);
    }

    /**
     * @return
     * @author qiushaohua 2012-3-18
     * @param userId
     */
    @Transactional(readOnly = true)
    public List<Company> getReceiverCompanys(int userId) {
        return companyRepository.getReceiverCompanys(userId);
    }

    @Override
    protected ResourceRepository<Company> getRepository() {
        return companyRepository;
    }
}
