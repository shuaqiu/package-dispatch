/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.repository.system.CompanyRepository;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Service
@Transactional
public class CompanyService {

    private CompanyRepository companyRepository;

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * @return
     * @author qiushaohua 2012-3-18
     * @param userId
     */
    public List<Company> getReceiverCompanys(int userId) {
        return companyRepository.getReceiverCompanys(userId);
    }

    /**
     * @return
     * @author qiushaohua 2012-3-24
     */
    public List<Company> getCustomerCompanys() {
        return companyRepository.getCustomerCompanys();
    }

}
