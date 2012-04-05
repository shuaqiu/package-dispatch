/**
 * @author qiushaohua 2012-4-4
 */
package com.qiuq.packagedispatch.repository.order;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.Order;
import com.qiuq.packagedispatch.bean.order.State;
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
            paramMap.addValue("receivedState", State.RECEIVED.ordinal());
            paramMap.addValue("cancelState", State.CANCELED.ordinal());
        }

        String query = Converter.toString(params.get("query"));
        if (StringUtils.hasText(query)) {
            sql += " and (sender_name like :query or sender_tel like :query or receiver_name like :query or receiver_tel like :query)";
            paramMap.addValue("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        return sql.replaceFirst(" and ", " where ");
    }

}
