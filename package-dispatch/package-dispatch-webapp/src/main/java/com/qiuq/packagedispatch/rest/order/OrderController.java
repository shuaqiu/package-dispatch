/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.rest.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.Result;
import com.qiuq.packagedispatch.bean.order.Order;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result save(@RequestBody Order order) {
        System.err.println(order);

        return Result.OK;
    }
}
