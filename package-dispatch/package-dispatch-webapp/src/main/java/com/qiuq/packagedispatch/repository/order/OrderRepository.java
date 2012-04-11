/**
 * @author qiushaohua 2012-4-4
 */
package com.qiuq.packagedispatch.repository.order;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.HandleDetail;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.ScheduleDetail;
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

    @Override
    public Order query(int id) {
        return doQuery("dispatch_order", id, BeanPropertyRowMapper.newInstance(Order.class));
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean insert(Order t) {
        String sql = "insert into dispatch_order(sender_id, sender_name, sender_tel, sender_company, sender_address,"
                + " receiver_id, receiver_name, receiver_tel, receiver_company, receiver_address,"
                + " goods_name, quantity, bar_code, sender_identity_code, receiver_identity_code, state, state_describe)"
                + "values(:senderId, :senderName, :senderTel, :senderCompany, :senderAddress,"
                + " :receiverId, :receiverName, :receiverTel, :receiverCompany, :receiverAddress,"
                + " :goodsName, :quantity, :barCode, :senderIdentityCode, :receiverIdentityCode, :state, :stateDescribe)";
        return doInsert(sql, new BeanPropertySqlParameterSource(t));
    }

    @Override
    public boolean update(int id, Order t) {
        return false;
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-4-4
     */
    public List<Order> query(String sort, Map<String, Object> params, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum from dispatch_order";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        List<Order> list = jdbcTemplate.query(rangeQuerySql, paramMap, BeanPropertyRowMapper.newInstance(Order.class));
        return list;
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
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
        String sql = "";
        if (params == null || params.size() == 0) {
            return sql;
        }

        int senderId = Converter.toInt(params.get("senderId"), -1);
        if (senderId != -1) {
            sql += " and sender_id = :senderId";
            paramMap.addValue("senderId", senderId);
        }

        int state = Converter.toInt(params.get("state"), -1);
        if (state != -1) {
            sql += " and state = :state";
            paramMap.addValue("state", state);
        }

        int processing = Converter.toInt(params.get("processing"), -1);
        if (processing != -1) {
            sql += " and state != :receivedState and state != :cancelState";
            paramMap.addValue("receivedState", State.DELIVERED.ordinal());
            paramMap.addValue("cancelState", State.CANCELED.ordinal());
        }

        String query = Converter.toString(params.get("query"));
        if (StringUtils.hasText(query)) {
            sql += " and (sender_name like :query or sender_tel like :query or receiver_name like :query or receiver_tel like :query)";
            paramMap.addValue("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        return sql.replaceFirst(" and ", " where ");
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
     * @param orderId
     * @param user
     * @author qiushaohua 2012-4-7
     * @return
     */
    public boolean updateScheduleInfo(int orderId, User user) {
        String sql = "update dispatch_order set scheduler_id = :schedulerId, scheduler_name = :schedulerName, scheduler_tel = :schedulerTel,"
                + " schedule_time = :scheduleTime, state = :state, state_describe = :stateDescribe where id = :id";

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("id", orderId);
        paramMap.addValue("schedulerId", user.getId());
        paramMap.addValue("schedulerName", user.getName());
        paramMap.addValue("schedulerTel", user.getTel());
        paramMap.addValue("scheduleTime", new Timestamp(System.currentTimeMillis()));
        paramMap.addValue("state", State.SCHEDULED.ordinal());
        paramMap.addValue("stateDescribe", State.SCHEDULED.getDescribe());
        return jdbcTemplate.update(sql, paramMap) == 1;
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-9
     */
    public List<ScheduleDetail> getScheduleDetail(int orderId) {
        String sql = "select * from dispatch_schedule_detail where order_id = :orderId";
        SqlParameterSource paramMap = new MapSqlParameterSource("orderId", orderId);
        return jdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(ScheduleDetail.class));
    }

    /**
     * @param orderId
     * @return
     * @author qiushaohua 2012-4-9
     */
    public List<HandleDetail> getHandleDetail(int orderId) {
        String sql = "select * from dispatch_handle_detail where order_id = :orderId";
        SqlParameterSource paramMap = new MapSqlParameterSource("orderId", orderId);
        return jdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(HandleDetail.class));
    }

}
