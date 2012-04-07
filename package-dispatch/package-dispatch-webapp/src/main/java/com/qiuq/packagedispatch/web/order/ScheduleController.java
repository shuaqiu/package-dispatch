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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.Role;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.order.OrderService;
import com.qiuq.packagedispatch.service.system.UserService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-4-5
 * @version 0.0.1
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController extends AbstractResourceController<Order> {

    private OrderService orderService;

    private UserService userService;

    /** @author qiushaohua 2012-4-5 */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /** @author qiushaohua 2012-4-6 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
     * @author qiushaohua 2012-4-5
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Order>> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", State.NEW_ORDER.ordinal());// only these new order need to be scheduled
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

    /**
     * @return
     * @author qiushaohua 2012-4-5
     */
    @RequestMapping(value = "/edit/{orderId}", method = RequestMethod.GET)
    public String edit(@PathVariable int orderId, Map<String, Object> r) {
        Order order = orderService.query(orderId);
        r.put("order", order);

        List<User> fetcher = userService.getUserWithRole(Role.FETCHER);
        List<User> transiter = userService.getUserWithRole(Role.TRANSITER);
        List<User> deliverer = userService.getUserWithRole(Role.DELIVERER);
        r.put("fetcher", fetcher);
        r.put("transiter", transiter);
        r.put("deliverer", deliverer);

        return "schedule/edit";
    }

    @RequestMapping(value = "/edit/{orderId}", method = RequestMethod.POST)
    @ResponseBody
    public OperateResult schedule(WebRequest req, @PathVariable int orderId, @RequestBody Map<String, Object> params) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if(user == null){
            return new OperateResult(ErrCode.INVALID, "not logined user");
        }

        int fetcherId = Converter.toInt(params.get("fetcher"));
        int delivererId = Converter.toInt(params.get("deliverer"));

        List<?> list = (List<?>) params.get("transiter");

        List<Integer> transiterIds = new ArrayList<Integer>(list.size());

        for (Object obj : list) {
            transiterIds.add(Converter.toInt(obj));
        }

        boolean isOk = orderService.schedule(user, orderId, fetcherId, transiterIds, delivererId);
        if (isOk) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.INSERT_FAIL, "add new resource fail");
        }
    }
}
