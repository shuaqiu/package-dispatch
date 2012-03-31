/**
 * @author qiushaohua 2012-3-20
 */
package com.qiuq.packagedispatch.repository.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceMapper;
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

    private final class UserResourceMapper implements ResourceMapper<User> {

        /**
         * @param user
         * @param forInsert
         * @return
         * @author qiushaohua 2012-3-28
         */
        @Override
        public MapSqlParameterSource mapObject(User user, SqlSourceType sourceType) {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();

            paramMap.addValue("name", user.getName());

            paramMap.addValue("tel", user.getTel());
            paramMap.addValue("company_id", user.getCompanyId());
            paramMap.addValue("company", user.getCompany());
            paramMap.addValue("department", user.getDepartment());
            paramMap.addValue("address", user.getAddress());

            paramMap.addValue("type", user.getType());
            if (user.getType() == Type.TYPE_CUSTOMER) {
                paramMap.addValue("customer_type", user.getCustomerType());
            } else {
                paramMap.addValue("customer_type", null, Types.INTEGER);
            }

            if (sourceType == SqlSourceType.INSERT) {
                paramMap.addValue("code", user.getCode());

                String salt = System.currentTimeMillis() + "";
                String password = passwordDecorder.encodePassword(user.getPassword(), salt);
                paramMap.addValue("password", password);
                paramMap.addValue("salt", salt);

                paramMap.addValue("state", User.STATE_VALID);
            }
            return paramMap;
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
     * @return
     * @author qiushaohua 2012-3-27
     */
    public List<User> query(String sort, String query) {
        String sql = "select * from sys_user where id > 0";
        SqlParameterSource paramMap = null;

        if (StringUtils.hasText(query)) {
            sql += " and (code like :query or name like :query or address like :query)";

            query = sqlUtil.escapeLikeValue(query);
            paramMap = new MapSqlParameterSource("query", "%" + query + "%");
        }

        sql += orderBy(sort);

        List<User> list = jdbcTemplate.query(sql, paramMap, new UserRowMapper());

        return list;
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
                + " values(:code, :name, :password, :salt, :tel, :company_id, :company, :department, :address, :type, :customer_type, :state)";

        return doInsert(sql, user, new UserResourceMapper());
    }

    @Override
    public boolean update(int id, User user) {
        String sql = "update sys_user set name = :name, tel = :tel, company_id = :company_id, company = :company,"
                + " department = :department, address = :address, type = :type, customer_type = :customer_type"
                + " where id = :id";

        return doUpdate(sql, id, user, new UserResourceMapper());
    }


}
