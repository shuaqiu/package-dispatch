/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.repository.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.qiuq.common.ErrCode;
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
            user.setUserCompanyId(rs.getInt("user_company_id"));
            user.setUserCompany(rs.getString("user_company"));
            user.setName(rs.getString("name"));
            user.setTel(rs.getString("tel"));
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

        sql += buildIntCondition(params, "userCompanyId", paramMap);
        sql += buildStringCondition(params, "userCompany", paramMap);

        sql += buildStringCondition(params, "name", paramMap);
        sql += buildStringCondition(params, "company", paramMap);
        sql += buildStringCondition(params, "address", paramMap);

        String query = Converter.toString(params.get("query"));
        sql += buildQueryCondition(query, paramMap, "user_company", "name", "tel", "company", "address");

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
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        return doInsert(getInsertSql(), new BeanPropertySqlParameterSource(receiver), generatedKeyHolder);
    }

    @Override
    public OperateResult update(Receiver t) {
        SqlParameterSource paramMap = new BeanPropertySqlParameterSource(t);
        OperateResult updateResult = doUpdate(getUpdateSql(), paramMap);

        if (updateResult.isOk()) {
            // update the receiver info in processing order
            jdbcTemplate.update(getUpdateOrderSql(), paramMap);
        }

        return updateResult;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-21
     */
    private String getInsertSql() {
        return "insert into customer_receiver(user_company_id, user_company, name, tel, company, address)"
                + " values(:userCompanyId, :userCompany, :name, :tel, :company, :address)";
    }

    /**
     * @return
     * @author qiushaohua 2012-5-21
     */
    private String getUpdateSql() {
        return "update customer_receiver set user_company_id = :userCompanyId, user_company = :userCompany,"
                + " name = :name, tel = :tel, company = :company, address = :address where id = :id";
    }

    /**
     * @return
     * @author qiushaohua 2012-5-21
     */
    private String getUpdateOrderSql() {
        return "update dispatch_order set receiver_name = :name, receiver_tel = :tel, receiver_company = :company, receiver_address = :address"
                + " where receiver_id = :id and state < " + State.DELIVERED.ordinal();
    }

    /**
     * @param list
     * @return
     * @author qiushaohua 2012-5-21
     */
    public OperateResult batchInsert(List<Receiver> list) {
        SqlParameterSource[] batchArgs = buildBatchArgs(list);

        int[] batchUpdate = jdbcTemplate.batchUpdate(getInsertSql(), batchArgs);

        List<Receiver> fails = new ArrayList<Receiver>();
        for (int i = 0; i < batchUpdate.length; i++) {
            if (batchUpdate[i] != 1) {
                fails.add(list.get(i));
            }
        }

        if (fails.size() == 0) {
            return OperateResult.OK;
        }

        return new OperateResult(ErrCode.INSERT_FAIL, "batch insert with fails", fails);
    }

    /**
     * @param list
     * @return
     * @author qiushaohua 2012-5-21
     */
    public OperateResult batchUpdate(List<Receiver> list) {
        SqlParameterSource[] batchArgs = buildBatchArgs(list);

        int[] batchUpdate = jdbcTemplate.batchUpdate(getUpdateSql(), batchArgs);

        List<SqlParameterSource> batchOrderArgs = new ArrayList<SqlParameterSource>();
        List<Receiver> fails = new ArrayList<Receiver>();
        for (int i = 0; i < batchUpdate.length; i++) {
            if (batchUpdate[i] != 1) {
                fails.add(list.get(i));
            } else {
                batchOrderArgs.add(batchArgs[i]);
            }
        }

        if (batchOrderArgs.size() > 0) {
            SqlParameterSource[] array = batchOrderArgs.toArray(new SqlParameterSource[batchOrderArgs.size()]);
            jdbcTemplate.batchUpdate(getUpdateOrderSql(), array);
        }

        if (fails.size() == 0) {
            return OperateResult.OK;
        }

        return new OperateResult(ErrCode.INSERT_FAIL, "batch insert with fails", fails);
    }

    /**
     * @param list
     * @return
     * @author qiushaohua 2012-5-21
     */
    private SqlParameterSource[] buildBatchArgs(List<Receiver> list) {
        SqlParameterSource[] batchArgs = new SqlParameterSource[list.size()];
        int index = 0;
        for (Receiver t : list) {
            batchArgs[index++] = new BeanPropertySqlParameterSource(t);
        }
        return batchArgs;
    }

}
