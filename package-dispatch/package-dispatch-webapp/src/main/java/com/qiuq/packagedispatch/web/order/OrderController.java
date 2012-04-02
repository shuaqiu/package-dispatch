/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.Order;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        return null;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public Map<String, Object> edit() {
        return null;
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
