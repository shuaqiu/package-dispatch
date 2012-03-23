/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.service.system.CompanyService;

/**
 * @author qiushaohua 2012-3-24
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/company")
public class CompanyController {

    private CompanyService companyService;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Company> list() {
        List<Company> coms = companyService.getCustomerCompanys();
        return coms;
    }

}
