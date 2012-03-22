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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.Result;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.CompanyService;
import com.qiuq.packagedispatch.service.system.UserService;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private CompanyService companyService;

    private UserService userService;


    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    /** @author qiushaohua 2012-3-21 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/new", method=RequestMethod.GET)
    public Map<String, Object> create() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("company", getReceiverCompany());
        return map;
    }

    /**
     * @return
     * @author qiushaohua 2012-3-21
     */
    private Map<Object, Object> getReceiverCompany() {
        Map<Object, Object> company = new LinkedHashMap<Object, Object>();

        List<Map<String, Object>> allCompany = companyService.getAll();
        for (Map<String, Object> aCom : allCompany) {
            company.put(aCom.get("id"), aCom.get("name"));
        }
        return company;
    }

    @RequestMapping(value = "/receiver", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getReceiver(WebRequest req) {
        User user = (User) req.getAttribute("user", RequestAttributes.SCOPE_SESSION);
        if (user == null) {
            return null;
        }
        return userService.getReceiverList(user.getCode());
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result save(@RequestBody Map<String, Object> map) {

        Order order = Converter.mapToBean(map, Order.class);
        System.err.println(order);

        Result result = Result.OK;

        return result;
    }
}
