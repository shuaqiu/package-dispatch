/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.repository.system;

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
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Repository
public class CompanyRepository extends AbstractRepository implements ResourceRepository<Company> {

    /**
     * @author qiushaohua 2012-3-24
     * @version 0.0.1
     */
    private final class CompanyRowMapper implements RowMapper<Company> {
        @Override
        public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company com = new Company();

            com.setId(rs.getInt("id"));
            com.setCode(rs.getString("code"));
            com.setName(rs.getString("name"));
            com.setAddress(rs.getString("address"));
            com.setParentId(rs.getInt("parent_id"));
            com.setFullId(rs.getString("full_id"));
            com.setType(rs.getInt("type"));

            return com;
        }
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-3-24
     */
    @Override
    public List<Company> query(String sort, Map<String, Object> params, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum from sys_company where id > 0";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        return jdbcTemplate.query(rangeQuerySql, paramMap, new CompanyRowMapper());
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-3
     */
    @Override
    public long matchedRecordCount(Map<String, Object> params) {
        String sql = "select count(*) from sys_company where id > 0";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        return jdbcTemplate.queryForLong(sql, paramMap);
    }

    /**
     * @param params
     * @param paramMap
     * @return
     * @author qiushaohua 2012-5-12
     */
    private String buildCondition(Map<String, Object> params, MapSqlParameterSource paramMap) {
        String sql = "";
        if (params == null || params.size() == 0) {
            return sql;
        }

        sql += buildStringCondition(params, "code", paramMap);
        sql += buildStringCondition(params, "name", paramMap);
        sql += buildStringCondition(params, "address", paramMap);

        String query = Converter.toString(params.get("query"));
        sql += buildQueryCondition(query, paramMap, "code", "name", "address");

        return sql;
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-3-26
     */
    @Override
    public Company query(int id) {
        return doQuery("sys_company", id, new CompanyRowMapper());
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-3-26
     */
    @Override
    public OperateResult delete(int id) {
        return doDelete("sys_company", id);
    }

    /**
     * @param com
     * @author qiushaohua 2012-3-25
     * @return
     */
    @Override
    public OperateResult insert(Company com) {
        String sql = "insert into sys_company(code, name, address, parent_id, full_id, type)"
                + " values(:code, :name, :address, :parentId, :fullId, :type)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        return doInsert(sql, new BeanPropertySqlParameterSource(com), generatedKeyHolder);
    }

    /**
     * @param com
     * @return
     * @author qiushaohua 2012-3-26
     */
    @Override
    public OperateResult update(Company com) {
        String sql = "update sys_company set code = :code, name = :name, address = :address where id = :id";

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(com);
        OperateResult updateResult = doUpdate(sql, paramSource);
        if (updateResult.isOk()) {
            // update the company info of user
            sql = "update sys_user set company = :name where company_id = :id";
            jdbcTemplate.update(sql, paramSource);

            // and update the processing order
            sql = "update dispatch_order set sender_company = :name"
                    + " where sender_id in (select id from sys_user where company_id = :id"
                    + "   and state < " + State.DELIVERED.ordinal();
            jdbcTemplate.update(sql, paramSource);
        }
        return updateResult;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-3
     */
    public int getMaxCode() {
        String sql = "select max(cast(code as int)) from sys_company";
        SqlParameterSource paramMap = null;
        int maxCode = jdbcTemplate.queryForInt(sql, paramMap);
        return maxCode;
    }

}
