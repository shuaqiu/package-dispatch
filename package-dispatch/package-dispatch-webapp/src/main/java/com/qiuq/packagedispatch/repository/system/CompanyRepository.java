/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.repository.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.repository.SqlServerSqlUtil;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Repository
public class CompanyRepository {

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

    private NamedParameterJdbcTemplate jdbcTemplate;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @return
     * @author qiushaohua 2012-3-24
     * @param sort
     * @param query
     */
    public List<Company> query(String sort, String query) {
        String sql = "select * from sys_company where id > 0";

        if (StringUtils.hasText(query)) {
            sql += " and (code like :query or name like :query or address like :query)";
        }

        if (StringUtils.hasText(sort) && sort.length() > 1) {
            if (sort.startsWith("+") || sort.startsWith(" ")) {
                sql += " order by " + sort.substring(1);
            } else if (sort.startsWith("-")) {
                sql += " order by " + sort.substring(1) + " desc";
            }
        }

        SqlParameterSource paramMap = null;
        if (StringUtils.hasText(query)) {
            query = SqlServerSqlUtil.escapeLikeCondition(query);
            paramMap = new MapSqlParameterSource("query", "%" + query + "%");
        }
        List<Company> companyList = jdbcTemplate.query(sql, paramMap, new CompanyRowMapper());

        return companyList;
    }

    /**
     * @param userId
     * @return
     * @author qiushaohua 2012-3-24
     */
    public List<Company> getReceiverCompanys(int userId) {
        String sql = "select com.* from sys_company com left join sys_user usr on com.id = usr.company_id"
                + " left join sys_sender_receiver relation on usr.id = relation.receiver_id"
                + " where relation.sender_id = :senderId";

        SqlParameterSource paramMap = new MapSqlParameterSource("senderId", userId);

        List<Company> companyList = jdbcTemplate.query(sql, paramMap, new CompanyRowMapper());
        return companyList;
    }

    /**
     * @param com
     * @author qiushaohua 2012-3-25
     * @return
     */
    public boolean insert(Company com) {
        String sql = "insert into sys_company(code, name, address, parent_id, full_id, type)"
                + " values(:code, :name, :address, :parent_id, :full_id, :type)";

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("code", com.getCode());
        paramMap.addValue("name", com.getName());
        paramMap.addValue("address", com.getAddress());
        paramMap.addValue("parent_id", -1);
        paramMap.addValue("full_id", -1);
        paramMap.addValue("type", Type.TYPE_CUSTOMER);

        try {
            int update = jdbcTemplate.update(sql, paramMap);
            return update == 1;
        } catch (DataAccessException e) {
        }
        return false;
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-3-26
     */
    public Company query(int id) {
        String sql = "select * from sys_company where id = :id";
        SqlParameterSource paramMap = new MapSqlParameterSource("id", id);

        try {
            Company com = jdbcTemplate.queryForObject(sql, paramMap, new CompanyRowMapper());
            return com;
        } catch (DataAccessException e) {
        }
        return null;
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-3-26
     */
    public boolean delete(int id) {
        String sql = "delete from sys_company where id = :id";
        SqlParameterSource paramMap = new MapSqlParameterSource("id", id);

        try {
            int update = jdbcTemplate.update(sql, paramMap);
            return update == 1;
        } catch (DataAccessException e) {
        }

        return false;
    }

    /**
     * @param id
     * @param com
     * @return
     * @author qiushaohua 2012-3-26
     */
    public boolean update(int id, Company com) {
        String sql = "update sys_company set code = :code, name = :name, address = :address where id = :id";

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("code", com.getCode());
        paramMap.addValue("name", com.getName());
        paramMap.addValue("address", com.getAddress());
        paramMap.addValue("id", id);

        try {
            int update = jdbcTemplate.update(sql, paramMap);
            return update == 1;
        } catch (DataAccessException e) {
        }
        return false;
    }

}
