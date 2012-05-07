/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.order.OrderService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-4-29
 * @version 0.0.1
 */
@Controller
@RequestMapping("/alarm")
public class AlarmController extends AbstractResourceController<Order> {

    private OrderService orderService;


    /** @author qiushaohua 2012-4-5 */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected ResourceService<Order> getService() {
        return orderService;
    }

    /**
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-4-29
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Order>> query(@RequestParam(defaultValue = "+fetchTime") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transiting", 1);
        params.put("query", query);

        long[] rangeArr = range(range);

        HttpHeaders header = new HttpHeaders();
        if (rangeArr != null) {
            long count = orderService.matchedRecordCount(params);
            header.set("Content-Range", " items " + (rangeArr[0] - 1) + "-" + (rangeArr[1] - 1) + "/" + count);
        }

        List<Order> list = orderService.query(sort, params, range(range));
        HttpEntity<List<Order>> entity = new HttpEntity<List<Order>>(list, header);

        return entity;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getNewAlarm(WebRequest req) {
        Map<String, Boolean> functionMap = HttpSessionUtil.getFunctionMap(req);
        if (functionMap == null) {
            return new ArrayList<Order>();
        }
        Boolean isHasFunction = functionMap.get("alarm");
        if (isHasFunction == null || !isHasFunction) {
            return new ArrayList<Order>();
        }

        return orderService.getNewAlarm();
    }
}
