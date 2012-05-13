/**
 * @author qiushaohua 2012-4-1
 */
package com.qiuq.packagedispatch.web.customer;

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

import com.qiuq.packagedispatch.bean.customer.ReceiverCompany;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.customer.ReceiverCompanyService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-4-1
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/receivercompany")
public class ReceiverCompanyController extends AbstractResourceController<ReceiverCompany> {

    private ReceiverCompanyService receiverCompanyService;

    /** @author qiushaohua 2012-4-1 */
    @Autowired
    public void setReceiverCompanyService(ReceiverCompanyService receiverCompanyService) {
        this.receiverCompanyService = receiverCompanyService;
    }

    @Override
    protected ResourceService<ReceiverCompany> getService() {
        return receiverCompanyService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<ReceiverCompany>> query(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new HttpEntity<List<ReceiverCompany>>(new ArrayList<ReceiverCompany>());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getId());
        params.put("query", query);

        return doQuery(sort, params, range);
    }
}
