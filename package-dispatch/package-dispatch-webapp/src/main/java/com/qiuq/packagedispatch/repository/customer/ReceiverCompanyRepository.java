/**
 * @author qiushaohua 2012-4-1
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
import com.qiuq.packagedispatch.bean.customer.ReceiverCompany;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-4-1
 * @version 0.0.1
 */
@Repository
public class ReceiverCompanyRepository extends AbstractRepository implements ResourceRepository<ReceiverCompany> {
    private final class ReceiverCompanyRowMapper implements RowMapper<ReceiverCompany> {
        @Override
        public ReceiverCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReceiverCompany com = new ReceiverCompany();
            com.setId(rs.getInt("id"));
            com.setUserId(rs.getInt("user_id"));
            com.setName(rs.getString("name"));
            com.setAddress(rs.getString("address"));
            return com;
        }
    }

    /**
     * @param userId
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-4-1
     */
    @Override
    public List<ReceiverCompany> query(String sort, Map<String, Object> params, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum from customer_receiver_company";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        return jdbcTemplate.query(rangeQuerySql, paramMap, new ReceiverCompanyRowMapper());
    }

    /**
     * @param userId
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    public long matchedRecordCount(Map<String, Object> params) {
        String sql = "select count(*) from customer_receiver_company";
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

        // must have condition
        sql += buildIntCondition(params, "userId", paramMap);

        sql += buildIntCondition(params, "companyId", paramMap);

        sql += buildStringCondition(params, "name", paramMap);
        sql += buildStringCondition(params, "address", paramMap);

        String query = Converter.toString(params.get("query"));
        sql += buildQueryCondition(query, paramMap, "name", "address");

        return sql.replaceFirst(" and ", " where ");
    }

    @Override
    public ReceiverCompany query(int id) {
        return doQuery("customer_receiver_company", id, new ReceiverCompanyRowMapper());
    }

    @Override
    public OperateResult delete(int id) {
        return doDelete("customer_receiver_company", id);
    }

    @Override
    public OperateResult insert(ReceiverCompany t) {
        String sql = "insert into customer_receiver_company(user_id, name, address) values(:userId, :name, :address)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        return doInsert(sql, new BeanPropertySqlParameterSource(t), generatedKeyHolder);
    }

    @Override
    public OperateResult update(ReceiverCompany t) {
        String sql = "update customer_receiver_company set user_id = :userId, name = :name, address = :address where id = :id";
        SqlParameterSource paramMap = new BeanPropertySqlParameterSource(t);
        OperateResult updateResult = doUpdate(sql, paramMap);

        if (updateResult.isOk()) {
            // update the company info of receiver
            sql = "update customer_receiver set company = :name where company_id = :id";
            jdbcTemplate.update(sql, paramMap);

            // and update the receiver company info in processing order
            sql = "update dispatch_order set receiver_company = :name"
                    + " where receiver_id in (select id from customer_receiver where company_id = :id)"
                    + "   and state < " + State.DELIVERED.ordinal();
            jdbcTemplate.update(sql, paramMap);
        }

        return updateResult;
    }

}
