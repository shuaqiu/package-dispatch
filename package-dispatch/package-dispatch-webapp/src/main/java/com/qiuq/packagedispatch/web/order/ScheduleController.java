/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.ScheduleDetail;
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
    public HttpEntity<List<Order>> query(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new HttpEntity<List<Order>>(new ArrayList<Order>());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", State.NEW_ORDER.ordinal());// only these new order need to be scheduled
        params.put("query", query);

        return doQuery(sort, params, range);
    }

    /**
     * @return
     * @author qiushaohua 2012-4-5
     */
    @RequestMapping(value = "/edit/{orderId}", method = RequestMethod.GET)
    public String edit(@PathVariable int orderId, Map<String, Object> r) {
        Order order = orderService.query(orderId);
        r.put("order", order);

        List<User> fetcherAndDeliverer = userService.getUserWithRole(Role.FETCHERS_AND_DELIVERERS);
        List<User> transiter = userService.getUserWithRole(Role.TRANSITERS);
        r.put("fetcher", fetcherAndDeliverer);
        r.put("transiter", transiter);
        r.put("deliverer", fetcherAndDeliverer);

        if (order.getState() >= State.SCHEDULED.ordinal()) {
            List<ScheduleDetail> scheduleDetail = orderService.getScheduleDetail(orderId);
            List<HandleDetail> handleDetail = orderService.getHandleDetail(orderId);

            Map<String, Object> scheduledFetcher = null;
            List<Map<String, Object>> scheduledTransiter = new ArrayList<Map<String, Object>>();
            Map<String, Object> scheduledDeliverer = null;

            int lastestHandledScheduleId = getLastestHandledScheduleId(handleDetail);

            // 如果没有对应的处理, 则所有调度都是尚未处理的
            boolean isHandled = lastestHandledScheduleId != -1;
            for (ScheduleDetail detail : scheduleDetail) {
                if (detail.getState() == State.FETCHED.ordinal()) {
                    // 这里是否已经处理增加一个对订单状态的判断
                    scheduledFetcher = toMap(detail, isHandled || order.getState() > State.FETCHED.ordinal());
                } else if (detail.getState() == State.TRANSITING.ordinal()) {
                    scheduledTransiter.add(toMap(detail, isHandled));
                } else if (detail.getState() == State.DELIVERED.ordinal()) {
                    scheduledDeliverer = toMap(detail, isHandled);
                }

                if (detail.getId() == lastestHandledScheduleId) {
                    // 这里基于这样的一个情况:
                    // 首先lastestHandledScheduleId, 标识已经处理到的调度ID(可能存在有实际处理和调度不一致的情况, 此时, 忽略找不到对应调度的处理)
                    // 如果有这样的一个调度, 它的ID 和lastestHandledScheduleId 是相等的
                    // 则这个调度之前的调度, 都应当是已经完成处理的(当然也会存在和调度不一致的情况, 但是这里忽略)
                    // 而在这个调度之后的调度, 则都是还没有处理的调度
                    isHandled = false;
                }
            }

            r.put("scheduledFetcher", scheduledFetcher);
            r.put("scheduledTransiter", scheduledTransiter);
            r.put("scheduledDeliverer", scheduledDeliverer);

            List<User> fetcher = new ArrayList<User>();
            List<User> deliverer = new ArrayList<User>();
            for (User user : fetcherAndDeliverer) {
                if (user.getId() != Converter.toInt(scheduledFetcher.get("id"))) {
                    fetcher.add(user);
                }
                if (user.getId() != Converter.toInt(scheduledDeliverer.get("id"))) {
                    deliverer.add(user);
                }
            }
            Map<Integer, User> m = new LinkedHashMap<Integer, User>();
            for (User user : transiter) {
                m.put(user.getId(), user);
            }
            for (Map<String, Object> t : scheduledTransiter) {
                m.remove(t.get("id"));
            }

            r.put("fetcher", fetcher);
            r.put("transiter", m.values());
            r.put("deliverer", deliverer);
        }

        return "schedule/edit";
    }

    /**
     * 获取最后的已经处理的调度ID, 这里直接从处理明细中, 判断是否有对应的调度ID, 如果有, 则返回最后的
     * 
     * @param handleDetail
     * @author qiushaohua 2012-5-17
     */
    private int getLastestHandledScheduleId(List<HandleDetail> handleDetail) {
        if(handleDetail == null || handleDetail.size() == 0){
            return -1;
        }
        ListIterator<HandleDetail> iterator = handleDetail.listIterator(handleDetail.size());
        while (iterator.hasPrevious()) {
            HandleDetail detail = iterator.previous();
            if (detail.getScheduleId() != null) {
                return detail.getScheduleId();
            }
        }
        return -1;
    }

    /**
     * @param detail
     * @param isHandled
     * @return
     * @author qiushaohua 2012-5-17
     */
    private Map<String, Object> toMap(ScheduleDetail detail, boolean isHandled) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", detail.getHandlerId());
        map.put("name", detail.getHandlerName());
        map.put("tel", detail.getHandlerTel());

        map.put("scheduleId", detail.getId());
        map.put("state", detail.getState());
        map.put("index", detail.getHandleIndex());

        map.put("handled", isHandled);
        return map;
    }

    @RequestMapping(value = "/edit/{orderId}", method = RequestMethod.POST)
    @ResponseBody
    public OperateResult schedule(WebRequest req, @PathVariable int orderId, @RequestBody Map<String, Object> params) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if(user == null){
            return new OperateResult(ErrCode.INVALID, "not logined user");
        }

        //        MultiValueMap<Integer, Integer> handlerMap = getHandlerMap(params);
        //        List<ScheduleDetail> details = buildScheduleDetails(orderId, handlerMap);
        List<ScheduleDetail> details = buildScheduleDetails(orderId, params);
        boolean isOk = orderService.schedule(user, orderId, details);
        if (isOk) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.INSERT_FAIL, "add new resource fail");
        }
    }

    @RequestMapping(value = "/edit/{orderId}", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult updateSchedule(WebRequest req, @PathVariable int orderId,
            @RequestBody Map<String, Object> params) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new OperateResult(ErrCode.INVALID, "not logined user");
        }

        //        MultiValueMap<Integer, Integer> handlerMap = getHandlerMap(params);
        //        List<ScheduleDetail> details = buildScheduleDetails(orderId, handlerMap);
        List<ScheduleDetail> details = buildScheduleDetails(orderId, params);

        List<ScheduleDetail> scheduleDetail = orderService.getScheduleDetail(orderId);
        List<HandleDetail> handleDetail = orderService.getHandleDetail(orderId);


        List<Integer> toDeleteScheduleIdList = new ArrayList<Integer>();

        int lastestHandledScheduleId = getLastestHandledScheduleId(handleDetail);
        // 如果没有对应的处理, 则所有调度都是尚未处理的
        boolean isHandled = lastestHandledScheduleId != -1;
        int index = 0;
        for (ScheduleDetail detail : scheduleDetail) {
            if (isHandled) {
                // 对于已经处理的调度, 则不用再更新调度, 则直接删除要插入的数据
                // XXX 这里有一种情况要注意: 如果在打开了修改调度的页面之后, 又增加了实际的处理明细, 那么就会出现数据不一致的情况的
                index++;
            } else {
                // 如果是没有处理的调度, 则进行删除(前面那些已经处理的调度, 则由于和处理明细有关联, 则不能删除)
                toDeleteScheduleIdList.add(detail.getId());
            }

            if (detail.getId() == lastestHandledScheduleId) {
                // 这里基于这样的一个情况:
                // 首先lastestHandledScheduleId, 标识已经处理到的调度ID(可能存在有实际处理和调度不一致的情况, 此时, 忽略找不到对应调度的处理)
                // 如果有这样的一个调度, 它的ID 和lastestHandledScheduleId 是相等的
                // 则这个调度之前的调度, 都应当是已经完成处理的(当然也会存在和调度不一致的情况, 但是这里忽略)
                // 而在这个调度之后的调度, 则都是还没有处理的调度
                isHandled = false;
            }
        }

        details = details.subList(index, details.size());

        boolean isOk = orderService.schedule(user, orderId, details, toDeleteScheduleIdList);
        if (isOk) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.INSERT_FAIL, "add new resource fail");
        }
    }

    /**
     * get the handler in each state from the parameter map
     * 
     * @param params
     * @return key is the ordinal of state, and the value is the id of handler
     * @author qiushaohua 2012-5-18
     */
    private MultiValueMap<Integer, Integer> getHandlerMap(Map<String, Object> params) {
        MultiValueMap<Integer, Integer> handlerMap = new LinkedMultiValueMap<Integer, Integer>();

        handlerMap.add(State.FETCHED.ordinal(), Converter.toInt(params.get("fetcher")));

        List<?> list = (List<?>) params.get("transiter");
        for (Object obj : list) {
            handlerMap.add(State.TRANSITING.ordinal(), Converter.toInt(obj));
        }

        handlerMap.add(State.DELIVERED.ordinal(), Converter.toInt(params.get("deliverer")));

        return handlerMap;
    }

    /**
     * @param orderId
     * @param handlerMap
     * @return
     * @author qiushaohua 2012-4-7
     */
    private List<ScheduleDetail> buildScheduleDetails(int orderId, MultiValueMap<Integer, Integer> handlerMap) {
        List<ScheduleDetail> details = new ArrayList<ScheduleDetail>();

        for (int state : handlerMap.keySet()) {
            List<Integer> handlerList = handlerMap.get(state);
            int index = 1;
            for (int handlerId : handlerList) {
                if (handlerId == -1) {
                    continue;
                }
                details.add(buildScheduleDetail(orderId, handlerId, index++, state));
            }
        }

        return details;
    }

    private List<ScheduleDetail> buildScheduleDetails(int orderId, Map<String, Object> params) {
        List<ScheduleDetail> details = new ArrayList<ScheduleDetail>();

        for (String state : params.keySet()) {
            @SuppressWarnings("unchecked")
            List<Object> handlerList = (List<Object>) params.get(state);
            int index = 1;
            for (Object obj : handlerList) {
                int handlerId = Converter.toInt(obj);
                if (handlerId == -1) {
                    continue;
                }
                details.add(buildScheduleDetail(orderId, handlerId, index++, State.valueOf(state).ordinal()));
            }
        }

        return details;
    }

    /**
     * @param orderId
     * @param handlerId
     * @param handleIndex
     * @param state
     * @return
     * @author qiushaohua 2012-4-7
     */
    private ScheduleDetail buildScheduleDetail(int orderId, int handlerId, int handleIndex, int state) {
        ScheduleDetail detail = new ScheduleDetail();
        detail.setOrderId(orderId);
        detail.setHandlerId(handlerId);
        detail.setHandleIndex(handleIndex);
        detail.setState(state);
        return detail;
    }
}
