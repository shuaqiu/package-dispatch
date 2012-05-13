/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.CompanyService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.CodeGenerator;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

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

    private CodeGenerator codeGenerator;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    /** @author qiushaohua 2012-5-3 */
    @Autowired
    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    protected ResourceService<Company> getService() {
        return companyService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Company>> query(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new HttpEntity<List<Company>>(new ArrayList<Company>());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("query", query);

        return doQuery(sort, params, range);
    }

    @Override
    protected OperateResult beforeInsert(Company t) {
        t.setCode(codeGenerator.generateCompanyCode());

        t.setParentId(-1);
        t.setFullId("-1");
        t.setType(Type.TYPE_CUSTOMER);

        return super.beforeInsert(t);
    }

}
