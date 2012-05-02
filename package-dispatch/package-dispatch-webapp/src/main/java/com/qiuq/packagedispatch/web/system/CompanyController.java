/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Override
    protected ResourceService<Company> getService() {
        return companyService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Company>> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        long[] rangeArr = range(range);

        HttpHeaders header = new HttpHeaders();
        if (rangeArr != null) {
            long count = companyService.matchedRecordCount(query);
            header.set("Content-Range", " items " + (rangeArr[0] - 1) + "-" + (rangeArr[1] - 1) + "/" + count);
        }

        List<Company> list = companyService.query(sort, query, rangeArr);
        HttpEntity<List<Company>> entity = new HttpEntity<List<Company>>(list, header);

        return entity;
    }

    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public Map<String, Object> edit() {
        Map<String, Object> rmap = new HashMap<String, Object>();
        rmap.put("generatedCode", generateCode());
        return rmap;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-2
     */
    private Object generateCode() {
        Set<String> codes = companyService.getCurrentCodes();

        return null;
    }
}
