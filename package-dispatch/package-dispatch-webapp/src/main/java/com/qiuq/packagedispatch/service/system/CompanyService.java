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
     * @param sort
     * @param query
     */
    public List<Company> query(String sort, String query) {
        return companyRepository.query(sort, query);
    }

    /**
     * @param com
     * @author qiushaohua 2012-3-25
     * @return
     */
    public boolean insert(Company com) {
        return companyRepository.insert(com);
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-3-26
     */
    public Company query(int id) {
        return companyRepository.query(id);
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-3-26
     */
    public boolean delete(int id) {
        return companyRepository.delete(id);
    }

    /**
     * @param id
     * @param com
     * @return
     * @author qiushaohua 2012-3-26
     */
    public boolean update(int id, Company com) {
        return companyRepository.update(id, com);
    }

}
