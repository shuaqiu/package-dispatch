/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.system.User;
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

    private int timeToNotDeliveredAlarm;
    private int timeToNotFetchAlarm;

    /**
     * 收件后, 经过下列时间(单位: 分钟)还未送达时, 将弹出警报窗口(多个时间用"," 号分隔)
     */
    private String timeToNoteNotDeliveredAlarm;
    /**
     * 调度后, 经过下列时间(单位: 分钟)还未上门收件时, 将弹出警报窗口(多个时间用"," 号分隔)
     */
    private String timeToNoteNotFetchAlarm;

    /** @author qiushaohua 2012-4-29 */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /** @author qiushaohua 2012-5-19 */
    @Value("${alarm.notdelivered.time}")
    public void setTimeToNotDeliveredAlarm(int timeToNotDeliveredAlarm) {
        this.timeToNotDeliveredAlarm = timeToNotDeliveredAlarm;
    }

    /** @author qiushaohua 2012-6-14 */
    @Value("${alarm.notfetch.time}")
    public void setTimeToNotFetchAlarm(int timeToNotFetchAlarm) {
        this.timeToNotFetchAlarm = timeToNotFetchAlarm;
    }

    /** @author qiushaohua 2012-6-14 */
    @Value("${alarm.notdelivered.time.note}")
    public void setTimeToNoteNotDeliveredAlarm(String timeToNoteNotDeliveredAlarm) {
        if (StringUtils.hasText(timeToNoteNotDeliveredAlarm)
                && timeToNoteNotDeliveredAlarm.matches("(\\d+)(,\\s*\\d+)*")) {
            this.timeToNoteNotDeliveredAlarm = timeToNoteNotDeliveredAlarm;
        }
    }

    /** @author qiushaohua 2012-6-14 */
    @Value("${alarm.notfetch.time.note}")
    public void setTimeToNoteNotFetchAlarm(String timeToNoteNotFetchAlarm) {
        if (StringUtils.hasText(timeToNoteNotFetchAlarm) && timeToNoteNotFetchAlarm.matches("(\\d+)(,\\s*\\d+)*")) {
            this.timeToNoteNotFetchAlarm = timeToNoteNotFetchAlarm;
        }
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
    public HttpEntity<List<Order>> query(WebRequest req, @RequestParam(defaultValue = "+fetchTime") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new HttpEntity<List<Order>>(new ArrayList<Order>());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("query", query);

        params.put("alarm", 1);
        params.put("fetchTime", timeToNotDeliveredAlarm);
        params.put("fetchTimeFormula", "datediff(mi, fetch_time, getdate())");
        params.put("fetchTimeOp", ">");
        params.put("scheduleTime", timeToNotFetchAlarm);
        params.put("scheduleTimeFormula", "datediff(mi, schedule_time, getdate())");
        params.put("scheduleTimeOp", ">");

        if (sort.endsWith("fetchTime") || sort.endsWith("scheduleTime")) {
            String order = sort.substring(0, 1);
            sort = order + "fetchTime," + order + "scheduleTime";
        }
        return doQuery(sort, params, range);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public OperateResult getNewAlarm(WebRequest req) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new OperateResult(ErrCode.NOT_LOGINED, "not logined user");
        }

        Map<String, Boolean> functionMap = HttpSessionUtil.getFunctionMap(req);
        if (isNotPermission(functionMap, "alarm")) {
            return new OperateResult(ErrCode.NOT_PERMISSION, "could not access alarm function");
        }

        List<Order> newAlarms = orderService.getNewAlarm(timeToNoteNotDeliveredAlarm, timeToNoteNotFetchAlarm);
        return new OperateResult(true, newAlarms);
    }
}
