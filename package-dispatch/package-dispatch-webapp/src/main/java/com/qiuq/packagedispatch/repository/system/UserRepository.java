/**
 * @author qiushaohua 2012-3-20
 */
package com.qiuq.packagedispatch.repository.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-3-20
 * @version 0.0.1
 */
@Repository
public class UserRepository extends AbstractRepository implements ResourceRepository<User> {

    /**
     * @author qiushaohua 2012-3-24
     * @version 0.0.1
     */
    private final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setCode(rs.getString("code"));
            user.setName(rs.getString("name"));
            user.setAlias(rs.getString("alias"));
            user.setPassword(rs.getString("password"));
            user.setSalt(rs.getString("salt"));
            user.setTel(rs.getString("tel"));
            user.setCompanyId(rs.getInt("company_id"));
            user.setCompany(rs.getString("company"));
            user.setDepartment(rs.getString("department"));
            user.setAddress(rs.getString("address"));
            user.setType(rs.getInt("type"));
            user.setCustomerType(rs.getInt("customer_type"));
            user.setState(rs.getInt("state"));
            return user;
        }
    }

    private PasswordEncoder passwordDecorder;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setPasswordDecorder(PasswordEncoder passwordDecorder) {
        this.passwordDecorder = passwordDecorder;
    }

    /**
     * @param usercode
     * @param password
     * @return
     * @author qiushaohua 2012-3-20
     */
    public User getLoginUser(String usercode, String password) {
        String sql = "select * from sys_user where code = :code";

        SqlParameterSource paramMap = new MapSqlParameterSource("code", usercode);
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, paramMap, new UserRowMapper());
        } catch (DataAccessException e) {
            return null;
        }

        if (user == null || user.getState() != User.STATE_VALID) {
            return null;
        }

        boolean isValid = passwordDecorder.isPasswordValid(user.getPassword(), password, user.getSalt());
        if (!isValid) {
            return null;
        }

        return user;
    }

    /**
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-3-27
     */
    public List<User> query(String sort, String query, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum from sys_user where id > 0";
        SqlParameterSource paramMap = null;

        if (StringUtils.hasText(query)) {
            sql += " and (code like :query or name like :query or address like :query)";
            paramMap = new MapSqlParameterSource("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        return jdbcTemplate.query(rangeQuerySql, paramMap, new UserRowMapper());
    }

    /**
     * @param query
     * @return
     * @author qiushaohua 2012-4-4
     */
    public long matchedRecordCount(String query) {
        String sql = "select count(*) from sys_user where id > 0";
        SqlParameterSource paramMap = null;

        if (StringUtils.hasText(query)) {
            sql += " and (code like :query or name like :query or address like :query)";
            paramMap = new MapSqlParameterSource("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        return jdbcTemplate.queryForLong(sql, paramMap);
    }

    @Override
    public User query(int id) {
        return doQuery("sys_user", id, new UserRowMapper());
    }

    @Override
    public boolean delete(int id) {
        return doDelete("sys_user", id);
    }

    @Override
    public boolean insert(User user) {
        String sql = "insert into sys_user(code, name, password, salt, tel, company_id, company, department, address, type, customer_type, state)"
                + " values(:code, :name, :password, :salt, :tel, :companyId, :company, :department, :address, :type, :customerType, :state)";

        String salt = System.currentTimeMillis() + "";
        String password = passwordDecorder.encodePassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(password);

        if (user.getType() == Type.TYPE_SELF) {
            user.setCustomerType(null);
        }

        user.setState(User.STATE_VALID);

        return doInsert(sql, new BeanPropertySqlParameterSource(user));
    }

    @Override
    public boolean update(int id, User user) {
        String sql = "update sys_user set name = :name, tel = :tel, company_id = :companyId, company = :company,"
                + " department = :department, address = :address, type = :type, customer_type = :customerType"
                + " where id = :id";

        return doUpdate(sql, new BeanPropertySqlParameterSource(user));
    }

}
