/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.repository.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.customer.Receiver;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-3-31
 * @version 0.0.1
 */
@Repository
public class ReceiverRepository extends AbstractRepository implements ResourceRepository<Receiver> {

    /**
     * @author qiushaohua 2012-3-24
     * @version 0.0.1
     */
    private final class ReceiverRowMapper implements RowMapper<Receiver> {
        @Override
        public Receiver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Receiver user = new Receiver();
            user.setId(rs.getInt("id"));
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            user.setTel(rs.getString("tel"));
            user.setCompanyId(rs.getInt("company_id"));
            user.setCompany(rs.getString("company"));
            user.setAddress(rs.getString("address"));
            return user;
        }
    }

    /**
     * @param userId
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-3-31
     */
    @Override
    public List<Receiver> query(String sort, Map<String, Object> params, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum from customer_receiver";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        List<Receiver> list = jdbcTemplate.query(rangeQuerySql, paramMap, new ReceiverRowMapper());
        return list;
    }

    /**
     * @param userId
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    public long matchedRecordCount(Map<String, Object> params) {
        String sql = "select count(*) from customer_receiver";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        return jdbcTemplate.queryForLong(sql, paramMap);
    }

    /**
     * @param params
     * @param paramMap
     * @return
     * @author qiushaohua 2012-5-13
     */
    private String buildCondition(Map<String, Object> params, MapSqlParameterSource paramMap) {
        String sql = "";
        if (params == null || params.size() == 0) {
            return sql;
        }

        sql += buildIntCondition(params, "userId", paramMap);

        sql += buildIntCondition(params, "companyId", paramMap);

        sql += buildStringCondition(params, "name", paramMap);
        sql += buildStringCondition(params, "company", paramMap);
        sql += buildStringCondition(params, "address", paramMap);

        String query = Converter.toString(params.get("query"));
        sql += buildQueryCondition(query, paramMap, "name", "tel", "company", "address");

        return sql.replaceFirst(" and ", " where ");
    }

    @Override
    public Receiver query(int id) {
        return doQuery("customer_receiver", id, new ReceiverRowMapper());
    }

    @Override
    public OperateResult delete(int id) {
        return doDelete("customer_receiver", id);
    }

    @Override
    public OperateResult insert(Receiver receiver) {
        String sql = "insert into customer_receiver(user_id, name, tel, company_id, company, address)"
                + " values(:userId, :name, :tel, :companyId, :company, :address)";

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        return doInsert(sql, new BeanPropertySqlParameterSource(receiver), generatedKeyHolder);
    }

    @Override
    public OperateResult update(Receiver t) {
        String sql = "update customer_receiver set user_id = :userId, name = :name, tel = :tel,"
                + " company_id = :companyId, company = :company, address = :address where id = :id";

        SqlParameterSource paramMap = new BeanPropertySqlParameterSource(t);
        OperateResult updateResult = doUpdate(sql, paramMap);

        if (updateResult.isOk()) {
            // update the receiver info in processing order
            sql = "update dispatch_order set receiver_name = :name, receiver_tel = :tel, receiver_company = :company, receiver_address = :address"
                    + " where receiver_id = :id and state < " + State.DELIVERED.ordinal();
            jdbcTemplate.update(sql, paramMap);
        }

        return updateResult;
    }

}
