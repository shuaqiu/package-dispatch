/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.CompanyService;
import com.qiuq.packagedispatch.web.AbstractResourceController;

/**
 * Manage the company for customers
 * 
 * @author qiushaohua 2012-3-24
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/company")
public class CompanyController extends AbstractResourceController<Company> {

    private CompanyService companyService;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Company> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        return companyService.query(sort, query, range(range));
    }

    @Override
    protected ResourceService<Company> getService() {
        return companyService;
    }
}
