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
import org.springframework.web.client.RestTemplate;

import com.qiuq.common.Result;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.service.system.CompanyService;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private CompanyService companyService;

    private RestTemplate restTemplate;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value="/new", method=RequestMethod.GET)
    public Map<String, Object> create() {
        Map<String, Object> map = new HashMap<String, Object>();

        Map<Object, Object> company = new LinkedHashMap<Object, Object>();

        List<Map<String, Object>> allCompany = companyService.getAll();
        for (Map<String, Object> aCom : allCompany) {
            company.put(aCom.get("id"), aCom.get("name"));
        }

        map.put("company", company);

        return map;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result save(@RequestBody Map<String, Object> map) {

        Order order = Converter.mapToBean(map, Order.class);
        System.err.println(order);

        Result result = restTemplate.postForObject("http://localhost:8080/SimplyWeb/api/order", order, Result.class);

        return result;
    }
}
