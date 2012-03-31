/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.CompanyService;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public Map<String, Object> create(WebRequest req) {
        Map<String, Object> map = new HashMap<String, Object>();
        User loginedUser = HttpSessionUtil.getLoginedUser(req);
        if (loginedUser != null) {
            Map<Object, Object> receiverCompany = getReceiverCompany(loginedUser.getId());
            map.put("company", receiverCompany);
        }

        return map;
    }

    /**
     * @return
     * @author qiushaohua 2012-3-21
     */
    private Map<Object, Object> getReceiverCompany(int userId) {
        Map<Object, Object> company = new LinkedHashMap<Object, Object>();

        List<Company> allCompany = companyService.getReceiverCompanys(userId);
        for (Company aCom : allCompany) {
            company.put(aCom.getId(), aCom.getName());
        }
        return company;
    }

    @RequestMapping(value = "/receiver", method = RequestMethod.GET)
    public Map<String, Object> receiver() {
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public OperateResult save(@RequestBody Map<String, Object> map) {
        Order order = Converter.mapToBean(map, Order.class);
        System.err.println(order);

        OperateResult result = OperateResult.OK;

        return result;
    }
}
