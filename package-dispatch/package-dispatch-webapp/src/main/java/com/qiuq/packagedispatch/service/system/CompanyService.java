/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.service.system;


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

    @Override
    protected ResourceRepository<Company> getRepository() {
        return companyRepository;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-3
     */
    @Transactional(readOnly = true)
    public int getMaxCode() {
        return companyRepository.getMaxCode();
    }
}
