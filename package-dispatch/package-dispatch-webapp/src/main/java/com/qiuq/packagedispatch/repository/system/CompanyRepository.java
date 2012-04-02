/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.repository.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceMapper;
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

    private final class CompanyResourceMapper implements ResourceMapper<Company> {

        @Override
        public MapSqlParameterSource mapObject(Company com, SqlSourceType sourceType) {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue("code", com.getCode());
            paramMap.addValue("name", com.getName());
            paramMap.addValue("address", com.getAddress());

            if (sourceType == SqlSourceType.INSERT) {
                paramMap.addValue("parent_id", -1);
                paramMap.addValue("full_id", -1);
                paramMap.addValue("type", Type.TYPE_CUSTOMER);
            }
            return paramMap;
        }

    }

    /**
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-3-24
     */
    public List<Company> query(String sort, String query, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum from sys_company where id > 0";
        SqlParameterSource paramMap = null;

        if (StringUtils.hasText(query)) {
            sql += " and (code like :query or name like :query or address like :query)";
            paramMap = new MapSqlParameterSource("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        List<Company> list = jdbcTemplate.query(rangeQuerySql, paramMap, new CompanyRowMapper());
        return list;
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
    public boolean delete(int id) {
        return doDelete("sys_company", id);
    }

    /**
     * @param com
     * @author qiushaohua 2012-3-25
     * @return
     */
    @Override
    public boolean insert(Company com) {
        String sql = "insert into sys_company(code, name, address, parent_id, full_id, type)"
                + " values(:code, :name, :address, :parent_id, :full_id, :type)";

        return doInsert(sql, com, new CompanyResourceMapper());
    }

    /**
     * @param id
     * @param com
     * @return
     * @author qiushaohua 2012-3-26
     */
    @Override
    public boolean update(int id, Company com) {
        String sql = "update sys_company set code = :code, name = :name, address = :address where id = :id";

        return doUpdate(sql, id, com, new CompanyResourceMapper());
    }

}
