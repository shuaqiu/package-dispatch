/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.service.system.CompanyService;

/**
 * Manage the company for customers
 * 
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        return null;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public Map<String, Object> create() {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Company> getCustomerCompanys(@RequestParam(required = false, defaultValue = "+id") String sort) {
        List<Company> coms = companyService.getCustomerCompanys();
        return coms;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OperateResult insert(@RequestBody Company com) {
        boolean isInserted = companyService.insert(com);
        if (isInserted) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.INSERT_FAIL, "add new company fail");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Company query(@PathVariable int id) {
        Company com = companyService.query(id);
        return com;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult update(@PathVariable int id, @RequestBody Company com) {
        boolean isUpdated = companyService.update(id, com);
        if (isUpdated) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.UPDATE_FAIL, "update fail");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult delete(@PathVariable int id) {
        boolean isDeleted = companyService.delete(id);
        if (isDeleted) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.DELETE_FAIL, "delete fail");
        }
    }
}
