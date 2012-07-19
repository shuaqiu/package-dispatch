/**
 * @author qiushaohua 2012-4-4
 */
package com.qiuq.packagedispatch.repository.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.ScheduleDetail;
import com.qiuq.packagedispatch.bean.order.ScheduleHistory;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-4-4
 * @version 0.0.1
 */
@Repository
public class OrderRepository extends AbstractRepository implements ResourceRepository<Order> {

    private final class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();

            order.setId(rs.getInt("id"));

            order.setSenderId(rs.getInt("sender_id"));
            order.setSenderName(rs.getString("sender_name"));
            order.setSenderTel(rs.getString("sender_tel"));
            order.setSenderCompany(rs.getString("sender_company"));
            order.setSenderAddress(rs.getString("sender_address"));

            order.setReceiverId(rs.getInt("receiver_id"));
            order.setReceiverName(rs.getString("receiver_name"));
            order.setReceiverTel(rs.getString("receiver_tel"));
            order.setReceiverCompany(rs.getString("receiver_company"));
            order.setReceiverAddress(rs.getString("receiver_address"));

            order.setOrderTime(rs.getTimestamp("order_time"));
            order.setGoodsName(rs.getString("goods_name"));
            order.setQuantity(rs.getString("quantity"));

            order.setBarCode(rs.getString("bar_code"));
            order.setSenderIdentityCode(rs.getString("sender_identity_code"));
            order.setReceiverIdentityCode(rs.getString("receiver_identity_code"));

            order.setFetchTime(rs.getTimestamp("fetch_time"));
            order.setDeliverTime(rs.getTimestamp("deliver_time"));

            // order.setSchedulerId(rs.getInt("schedule_id"));
            order.setSchedulerName(rs.getString("scheduler_name"));
            order.setSchedulerTel(rs.getString("scheduler_tel"));
            order.setScheduleTime(rs.getTimestamp("schedule_time"));

            // order.setHandlerId(rs.getInt("handler_id"));
            // order.setHandlerName(rs.getString("handler_name"));
            // order.setHandlerTel(rs.getString("handler_tel"));

            order.setState(rs.getInt("state"));
            order.setStateDescribe(rs.getString("state_describe"));

            return order;
        }
    }

    private final class ScheduleDetailRowMapper implements RowMapper<ScheduleDetail> {
        @Override
        public ScheduleDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScheduleDetail detail = new ScheduleDetail();

            detail.setId(rs.getInt("id"));
            detail.setOrderId(rs.getInt("order_id"));

            detail.setState(rs.getInt("state"));
            detail.setHandleIndex(rs.getInt("handle_index"));

            detail.setHandlerId(rs.getInt("handler_id"));
            detail.setHandlerName(rs.getString("handler_name"));
            detail.setHandlerTel(rs.getString("handler_tel"));

            detail.setMemo(rs.getString("memo"));

            return detail;
        }
    }

    private final class HandleDetailRowMapper implements RowMapper<HandleDetail> {
        @Override
        public HandleDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            HandleDetail detail = new HandleDetail();

            detail.setId(rs.getInt("id"));
            detail.setOrderId(rs.getInt("order_id"));

            detail.setState(rs.getInt("state"));
            detail.setHandleIndex(rs.getInt("handle_index"));

            detail.setHandlerId(rs.getInt("handler_id"));
            detail.setHandlerName(rs.getString("handler_name"));
            detail.setHandlerTel(rs.getString("handler_tel"));

            detail.setMemo(rs.getString("memo"));

            detail.setHandleTime(rs.getTimestamp("handle_time"));
            detail.setScheduleId(rs.getInt("schedule_id"));
            detail.setDescription(rs.getString("description"));

            return detail;
        }
    }

    private final class ScheduleHistoryRowMapper implements RowMapper<ScheduleHistory> {
        @Override
        public ScheduleHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScheduleHistory history = new ScheduleHistory();

            history.setId(rs.getInt("id"));
            history.setOrderId(rs.getInt("order_id"));

            history.setSchedulerName(rs.getString("scheduler_name"));
            history.setSchedulerTel(rs.getString("scheduler_tel"));
            history.setScheduleTime(rs.getTimestamp("schedule_time"));

            return history;
        }
    }

    @Override
    public OperateResult delete(int id) {
        throw new UnsupportedOperationException("delete an order is not supported");
    }

    @Override
    public OperateResult update(Order t) {
        throw new UnsupportedOperationException("delete an order is not supported");
    }

    @Override
    public Order query(int id) {
        return doQuery("dispatch_order", id, new OrderRowMapper());
    }

    @Override
    public OperateResult insert(Order t) {
        String sql = "insert into dispatch_order(sender_id, sender_name, sender_tel, sender_company, sender_address,"
                + " receiver_id, receiver_name, receiver_tel, receiver_company, receiver_address,"
                + " goods_name, quantity, bar_code, sender_identity_code, receiver_identity_code, state, state_describe)"
                + "values(:senderId, :senderName, :senderTel, :senderCompany, :senderAddress,"
                + " :receiverId, :receiverName, :receiverTel, :receiverCompany, :receiverAddress,"
                + " :goodsName, :quantity, :barCode, :senderIdentityCode, :receiverIdentityCode, :state, :stateDescribe)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        return doInsert(sql, new BeanPropertySqlParameterSource(t), generatedKeyHolder);
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    public List<Order> query(String sort, Map<String, Object> params, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum from dispatch_order";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        List<Order> list = jdbcTemplate.query(rangeQuerySql, paramMap, new OrderRowMapper());
        return list;
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    public long matchedRecordCount(Map<String, Object> params) {
        String sql = "select count(*) from dispatch_order";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        return jdbcTemplate.queryForLong(sql, paramMap);
    }

    /**
     * @param params
     * @param paramMap
     * @return
     * @author qiushaohua 2012-4-4
     */
    private String buildCondition(Map<String, Object> params, MapSqlParameterSource paramMap) {
        if (params == null || params.size() == 0) {
            return "";
        }

        StringBuilder sql = new StringBuilder();
        sql.append(buildIntCondition(params, "senderId", paramMap));
        sql.append(buildCondition(params, "state", paramMap));

        int alarm = Converter.toInt(params.get("alarm"), -1);
        if (alarm != -1) {
            sql.append(" and (");
            sql.append("  (state in (:fetchedState, :transitingState, :outStorageState)"
                    + buildIntCondition(params, "fetchTime", paramMap) + ")");
            sql.append("  or ");
            sql.append("  (state = :scheduledState" + buildIntCondition(params, "scheduleTime", paramMap) + ")");
            sql.append(" )");
            paramMap.addValue("fetchedState", State.FETCHED.ordinal());
            paramMap.addValue("transitingState", State.TRANSITING.ordinal());
            paramMap.addValue("outStorageState", State.OUT_STORAGE.ordinal());
            paramMap.addValue("scheduledState", State.SCHEDULED.ordinal());
        }

        String query = Converter.toString(params.get("query"));
        Map<String, String> fieldCondition = new HashMap<String, String>();
        fieldCondition.put("sender_identity_code", "state < 2");
        sql.append(buildQueryCondition(query, paramMap, fieldCondition, "sender_identity_code", "bar_code",
                "sender_name", "sender_tel", "receiver_name", "receiver_tel"));

        return sql.toString().replaceFirst(" and ", " where ");
    }

    /**
     * @param details
     * @return
     * @author qiushaohua 2012-4-7
     */
    public boolean insertScheduleDetails(List<ScheduleDetail> details) {
        String sql = "insert into dispatch_schedule_detail (order_id, state, handle_index, handler_id)"
                + " values (:orderId, :state, :handleIndex, :handlerId)";

        SqlParameterSource[] batchArgs = new SqlParameterSource[details.size()];

        int index = 0;
        for (ScheduleDetail detail : details) {
            batchArgs[index++] = new BeanPropertySqlParameterSource(detail);
        }

        int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);

        return batchUpdate != null;
    }

    /**
     * @param scheduleIdList
     * @return
     * @author qiushaohua 2012-5-19
     */
    public boolean deleteScheduleDetail(List<Integer> scheduleIdList) {
        String sql = "delete from dispatch_schedule_detail where id = :id";

        SqlParameterSource[] batchArgs = new SqlParameterSource[scheduleIdList.size()];

        int index = 0;
        for (Integer scheduleId : scheduleIdList) {
            batchArgs[index++] = new MapSqlParameterSource("id", scheduleId);
        }

        int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);

        return batchUpdate != null;

    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-7
     */
    public int updateHandlerInfo(int orderId) {
        String sql = "update dispatch_schedule_detail set"
                + " handler_name = (select usr.name from sys_user usr where usr.id = handler_id),"
                + " handler_tel = (select usr.tel from sys_user usr where usr.id = handler_id)"
                + " where order_id = :orderId";
        SqlParameterSource paramMap = new MapSqlParameterSource("orderId", orderId);
        return jdbcTemplate.update(sql, paramMap);
    }

    /**
     * update the scheduler info in order
     *
     * @param orderId
     * @param user
     * @return
     * @author qiushaohua 2012-6-5
     */
    public boolean updateOrderSchedulerInfo(int orderId, User user) {
        StringBuilder sql = new StringBuilder();
        sql.append("update dispatch_order set");
        sql.append("  scheduler_id = :schedulerId");
        sql.append(", scheduler_name = :schedulerName");
        sql.append(", scheduler_tel = :schedulerTel");
        sql.append(", schedule_time = :scheduleTime");
        sql.append(" where id = :id");

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("id", orderId);
        paramMap.addValue("schedulerId", user.getId());
        paramMap.addValue("schedulerName", user.getName());
        paramMap.addValue("schedulerTel", user.getTel());
        paramMap.addValue("scheduleTime", new Timestamp(System.currentTimeMillis()));
        return jdbcTemplate.update(sql.toString(), paramMap) == 1;
    }

    /**
     * @param orderId
     * @param user
     * @param fetcherId
     * @author qiushaohua 2012-4-7
     * @return
     */
    public boolean updateOrderInfo(int orderId, User user, int fetcherId) {
        StringBuilder sql = new StringBuilder();
        sql.append("update dispatch_order set");
        sql.append("  scheduler_id = :schedulerId");
        sql.append(", scheduler_name = :schedulerName");
        sql.append(", scheduler_tel = :schedulerTel");
        sql.append(", schedule_time = :scheduleTime");
        sql.append(", handler_id = :fetcherId");
        sql.append(", handler_name = (select name from sys_user where id = :fetcherId)");
        sql.append(", handler_tel = (select tel from sys_user where id = :fetcherId)");
        sql.append(", state = :state");
        sql.append(", state_describe = :stateDescribe");
        sql.append(" where id = :id");

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("id", orderId);
        paramMap.addValue("schedulerId", user.getId());
        paramMap.addValue("schedulerName", user.getName());
        paramMap.addValue("schedulerTel", user.getTel());
        paramMap.addValue("scheduleTime", new Timestamp(System.currentTimeMillis()));

        paramMap.addValue("fetcherId", fetcherId);

        paramMap.addValue("state", State.SCHEDULED.ordinal());
        paramMap.addValue("stateDescribe", State.SCHEDULED.getDescribe());
        return jdbcTemplate.update(sql.toString(), paramMap) == 1;
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-9
     */
    public List<ScheduleDetail> getScheduleDetail(int orderId) {
        String sql = "select * from dispatch_schedule_detail where order_id = :orderId order by state, handle_index";
        SqlParameterSource paramMap = new MapSqlParameterSource("orderId", orderId);
        return jdbcTemplate.query(sql, paramMap, new ScheduleDetailRowMapper());
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-9
     */
    public List<HandleDetail> getHandleDetail(int orderId) {
        // String sql = "select * from dispatch_handle_detail where order_id = :orderId order by state, handle_index";
        String sql = "select * from dispatch_handle_detail where order_id = :orderId order by handle_time";
        SqlParameterSource paramMap = new MapSqlParameterSource("orderId", orderId);
        return jdbcTemplate.query(sql, paramMap, new HandleDetailRowMapper());
    }

    /**
     * @param details
     * @return
     * @author qiushaohua 2012-4-24
     */
    public int[] batchHandle(List<HandleDetail> details) {
        String sql = "insert into dispatch_handle_detail (order_id, state, handle_index, handler_id, handler_name, handler_tel, handle_time, description)"
                + " values (:orderId, :state, :handleIndex, :handlerId, :handlerName, :handlerTel, :handleTime, :description)";

        SqlParameterSource[] batchArgs = new SqlParameterSource[details.size()];

        int index = 0;
        for (HandleDetail detail : details) {
            batchArgs[index++] = new BeanPropertySqlParameterSource(detail);
        }

        int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);
        return batchUpdate;
    }

    /**
     * @param barcode
     * @return
     * @author qiushaohua 2012-4-26
     */
    public Order getOrder(String barcode) {
        String sql = "select * from dispatch_order where bar_code = :barcode";
        SqlParameterSource paramMap = new MapSqlParameterSource("barcode", barcode);
        try {
            return jdbcTemplate.queryForObject(sql, paramMap, new OrderRowMapper());
        } catch (DataAccessException e) {
            // not such bar code or more than one order has such bar code
            return null;
        }
    }

    /**
     * @param detail
     * @author qiushaohua 2012-4-26
     * @return
     */
    public boolean insertHandleDetail(HandleDetail detail) {
        String sql = "insert into dispatch_handle_detail (order_id, state, handle_index, handler_id, handler_name, handler_tel, handle_time, description)"
                + " values (:orderId, :state, :handleIndex, :handlerId, :handlerName, :handlerTel, :handleTime, :description)";
        SqlParameterSource paramMap = new BeanPropertySqlParameterSource(detail);

        int inserted = jdbcTemplate.update(sql, paramMap);
        return inserted == 1;

    }

    /**
     * @param orderId
     * @param handler
     * @param state
     * @author qiushaohua 2012-4-26
     */
    public boolean updateOrderState(int orderId, User handler, State state) {
        StringBuilder sql = new StringBuilder();
        sql.append("update dispatch_order set");
        sql.append("  handler_id = :handlerId");
        sql.append(", handler_name = :handlerName");
        sql.append(", handler_tel = :handlerTel");
        sql.append(", state = :state");
        sql.append(", state_describe = :stateDescribe");
        sql.append(" where id = :id");

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("handlerId", handler.getId());
        paramMap.addValue("handlerName", handler.getName());
        paramMap.addValue("handlerTel", handler.getTel());
        paramMap.addValue("state", state.ordinal());
        paramMap.addValue("stateDescribe", state.getDescribe());
        paramMap.addValue("id", orderId);

        return jdbcTemplate.update(sql.toString(), paramMap) == 1;
    }

    /**
     * @param orderId
     * @param receiverIdentityCode
     * @return
     * @author qiushaohua 2012-4-28
     */
    public boolean updateReceiverIdentiry(int orderId, String receiverIdentityCode) {
        String sql = "update dispatch_order set receiver_identity_code = :receiverIdentityCode where id = :orderId";

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("receiverIdentityCode", receiverIdentityCode);
        paramMap.addValue("orderId", orderId);

        return jdbcTemplate.update(sql.toString(), paramMap) == 1;
    }

    /**
     * @return
     * @author qiushaohua 2012-4-28
     */
    public Set<String> getCurrentSenderIdentity() {
        String sql = "select sender_identity_code from dispatch_order where state = :newOrderState or state = :scheduledState";

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("newOrderState", State.NEW_ORDER.ordinal());
        paramMap.addValue("scheduledState", State.SCHEDULED.ordinal());

        return new HashSet<String>(jdbcTemplate.queryForList(sql, paramMap, String.class));
    }

    /**
     * @return
     * @author qiushaohua 2012-4-29
     */
    public List<Order> getNewAlarm(String timeToNoteNotDeliveredAlarm, String timeToNoteNotFetchAlarm) {
        StringBuilder sql = new StringBuilder("select * from dispatch_order");
        sql.append(" where (");
        sql.append("  (state in (:fetchedState, :transitingState, :outStorageState)");
        sql.append("   and datediff(mi, fetch_time, getdate()) in (" + timeToNoteNotDeliveredAlarm + "))");
        sql.append(" or ");
        sql.append("  (state = :scheduleState and datediff(mi, schedule_time, getdate()) in ("
                + timeToNoteNotFetchAlarm + "))");
        sql.append(") order by fetch_time, schedule_time");
        // + "   and datediff(mi, fetch_time, getdate()) > 45 order by fetch_time"; // for test
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("fetchedState", State.FETCHED.ordinal());
        paramMap.addValue("transitingState", State.TRANSITING.ordinal());
        paramMap.addValue("outStorageState", State.OUT_STORAGE.ordinal());
        paramMap.addValue("scheduleState", State.SCHEDULED.ordinal());
        return jdbcTemplate.query(sql.toString(), paramMap, new OrderRowMapper());
    }

    /**
     * @param t
     * @return
     * @author qiushaohua 2012-6-3
     */
    public OperateResult cancelOrClose(Order t) {
        String sql = "update dispatch_order set state = :state, state_describe = :stateDescribe where id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("state", t.getState());
        paramMap.addValue("stateDescribe", t.getStateDescribe());
        paramMap.addValue("id", t.getId());

        return doUpdate(sql, paramMap);
    }

    /**
     * @return
     * @author qiushaohua 2012-6-3
     */
    public Map<Integer, Integer> getScheduledTaskCount() {
        String sql = "select d.handler_id as handlerId, count(*) as taskCount from dispatch_schedule_detail d, dispatch_order o"
                + " where d.order_id = o.id and o.state >= 1 and o.state <= 5 group by d.handler_id";
        return getTaskCount(sql);
    }

    /**
     * @return
     * @author qiushaohua 2012-6-3
     */
    public Map<Integer, Integer> getHandledTaskCount() {
        String sql = "select d.handler_id as handlerId, count(*) as taskCount from dispatch_handle_detail d, dispatch_order o"
                + " where d.order_id = o.id and o.state >= 1 and o.state <= 5 group by d.handler_id";
        return getTaskCount(sql);
    }

    /**
     * @param sql
     * @return
     * @author qiushaohua 2012-6-3
     */
    private Map<Integer, Integer> getTaskCount(String sql) {
        SqlParameterSource paramMap = null;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, paramMap);

        Map<Integer, Integer> taskCount = new HashMap<Integer, Integer>();
        for (Map<String, Object> m : list) {
            taskCount.put(Converter.toInt(m.get("handlerId")), Converter.toInt(m.get("taskCount")));
        }

        return taskCount;
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-6-8
     */
    public boolean insertScheduleHistory(int orderId) {
        String sql = "insert into dispatch_schedule_history (order_id, scheduler_name, scheduler_tel, schedule_time)"
                + " select id, scheduler_name, scheduler_tel, schedule_time from dispatch_order where id = :orderId";

        SqlParameterSource paramMap = new MapSqlParameterSource("orderId", orderId);
        int batchUpdate = jdbcTemplate.update(sql, paramMap);

        return batchUpdate == 1;
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-6-8
     */
    public List<ScheduleHistory> getScheduleHistory(int orderId) {
        String sql = "select * from dispatch_schedule_history where order_id = :orderId order by id";
        SqlParameterSource paramMap = new MapSqlParameterSource("orderId", orderId);
        return jdbcTemplate.query(sql, paramMap, new ScheduleHistoryRowMapper());
    }
}
