/**
 * @author qiushaohua 2012-3-20
 */
package com.qiuq.packagedispatch.repository.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-3-20
 * @version 0.0.1
 */
@Repository
public class UserRepository extends AbstractRepository implements ResourceRepository<Map<String, Object>> {

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
            user.setLoginAccount(rs.getString("login_account"));
            user.setPassword(rs.getString("password"));
            user.setSalt(rs.getString("salt"));
            user.setTel(rs.getString("tel"));
            user.setShortNumber(rs.getString("short_number"));
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

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /** @author qiushaohua 2012-5-12 */
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * @param usercode
     * @param password
     * @return
     * @author qiushaohua 2012-3-20
     */
    public User getLoginUser(String usercode, String password) {
        User user = getUser(usercode);
        if (user == null || user.getState() != User.STATE_VALID) {
            return null;
        }

        boolean isValid = passwordEncoder.isPasswordValid(user.getPassword(), password, user.getSalt());
        if (!isValid) {
            return null;
        }

        return user;
    }

    /**
     * @param code
     * @return
     * @author qiushaohua 2012-4-28
     */
    public User getUser(String usercode) {
        String sql = "select * from sys_user where code = :usercode or login_account = :usercode";

        SqlParameterSource paramMap = new MapSqlParameterSource("usercode", usercode);
        try {
            return jdbcTemplate.queryForObject(sql, paramMap, new UserRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-3-27
     */
    @Override
    public List<Map<String, Object>> query(String sort, Map<String, Object> params, long[] range) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("  usr.id as id");
        sql.append(", usr.code as code");
        sql.append(", usr.login_account as loginAccount");
        sql.append(", usr.name as name");
        sql.append(", usr.tel as tel");
        sql.append(", usr.short_number as shortNumber");
        sql.append(", usr.company_id as companyId");
        sql.append(", usr.company as company");
        sql.append(", usr.department as department");
        sql.append(", usr.address as address");
        sql.append(", usr.type as type");
        sql.append(", usr.customer_type as customerType");
        sql.append(", usr.state as state");
        sql.append(", role.role_id as roleId");
        sql.append(", row_number() over(" + orderBy(sort) + ") as rownum");
        sql.append(" from sys_user usr left join sys_user_role role on usr.id = role.user_id");
        sql.append(" where usr.id > 0 and usr.state = " + User.STATE_VALID);

        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql.append(buildCondition(params, paramMap));

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql.toString(), range);
        return jdbcTemplate.query(rangeQuerySql, paramMap, new ColumnMapRowMapper());
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    public long matchedRecordCount(Map<String, Object> params) {
        String sql = "select count(*) from sys_user usr where usr.id > 0 and usr.state = " + User.STATE_VALID;
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        return jdbcTemplate.queryForLong(sql, paramMap);
    }

    /**
     * @param params
     * @param paramMap
     * @return
     * @author qiushaohua 2012-4-8
     */
    private String buildCondition(Map<String, Object> params, MapSqlParameterSource paramMap) {
        String sql = "";
        if (params == null || params.size() == 0) {
            return sql;
        }

        sql += buildIntCondition(params, "type", "usr", paramMap);
        sql += buildIntCondition(params, "companyId", "usr", paramMap);

        String query = Converter.toString(params.get("query"));
        sql += buildQueryCondition(query, paramMap, "usr.code", "usr.login_account", "usr.name", "usr.address");

        return sql;
    }

    /**
     * @param roleId
     * @return
     * @author qiushaohua 2012-4-6
     */
    public List<User> getUserWithRole(int roleId) {
        String sql = "select usr.* from sys_user usr left join sys_user_role role on usr.id = role.user_id"
                + " where usr.type = " + Type.TYPE_SELF + " and role.role_id = :roleId";
        SqlParameterSource paramMap = new MapSqlParameterSource("roleId", roleId);

        return jdbcTemplate.query(sql, paramMap, new UserRowMapper());
    }

    /**
     * @param user
     * @param newPassword
     * @return
     * @author qiushaohua 2012-4-23
     */
    public OperateResult modifyPassword(User user, String newPassword) {
        String salt = createSalt();
        String password = passwordEncoder.encodePassword(newPassword, salt);

        String sql = "update sys_user set password = :password, salt = :salt where id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("password", password);
        paramMap.addValue("salt", salt);
        paramMap.addValue("id", user.getId());
        int updated = jdbcTemplate.update(sql, paramMap);

        if (updated == 1) {
            user.setSalt(salt);
            user.setPassword(password);
            return OperateResult.OK;
        }

        return new OperateResult(ErrCode.UPDATE_FAIL, "Update password fail");
    }

    /**
     * @param loginAccount
     * @param id
     * @return
     * @author qiushaohua 2012-5-3
     */
    public int getUserCount(String loginAccount, int id) {
        String sql = "select count(*) from sys_user where login_account = :loginAccount";
        MapSqlParameterSource paramMap = new MapSqlParameterSource("loginAccount", loginAccount);

        if (id != -1) {
            sql += " and id != :id";
            paramMap.addValue("id", id);
        }

        return jdbcTemplate.queryForInt(sql, paramMap);
    }

    @Override
    public Map<String, Object> query(int id) {
        return doQuery("sys_user", id, new ColumnMapRowMapper());
    }

    @Override
    public OperateResult delete(int id) {
        OperateResult isDeleted = doDelete("sys_user", id);
        if (isDeleted.isOk()) {
            deleteRole(id);
        }
        // TODO need to update those table that relative with user

        return isDeleted;
    }

    private boolean deleteRole(int id) {
        String sql = "delete from sys_user_role where user_id = :userId";

        MapSqlParameterSource paramMap = new MapSqlParameterSource("userId", id);
        jdbcTemplate.update(sql, paramMap);
        return true;
    }

    @Override
    public OperateResult insert(Map<String, Object> map) {
        String sql = "insert into sys_user(code, login_account, name, password, salt, tel, short_number, company_id, company, department, address, type, customer_type, state)"
                + " values(:code, :loginAccount, :name, :password, :salt, :tel, :shortNumber, :companyId, :company, :department, :address, :type, :customerType, :state)";

        MapSqlParameterSource paramMap = buildUserParamMap(map);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        OperateResult isUserInserted = doInsert(sql, paramMap, generatedKeyHolder);

        if (isUserInserted.isOk() && map.containsKey("roleId")) {
            int id = generatedKeyHolder.getKey().intValue();
            map.put("id", id);
            return roleRepository.insert(map);
        }

        return isUserInserted;
    }

    /**
     * @param map
     * @return
     * @author qiushaohua 2012-5-12
     */
    private MapSqlParameterSource buildUserParamMap(Map<String, Object> map) {
        String salt = createSalt();
        String password = passwordEncoder.encodePassword(Converter.toString(map.get("password")), salt);

        int type = Converter.toInt(map.get("type"));
        Integer customerType = Converter.toInt(map.get("customerType"));
        if (type == Type.TYPE_SELF) {
            customerType = null;
        }

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("code", map.get("code"));
        paramMap.addValue("loginAccount", map.get("loginAccount"));
        paramMap.addValue("name", map.get("name"));
        paramMap.addValue("password", password);
        paramMap.addValue("salt", salt);
        paramMap.addValue("tel", map.get("tel"));
        paramMap.addValue("shortNumber", map.get("shortNumber"));
        paramMap.addValue("companyId", map.get("companyId"));
        paramMap.addValue("company", map.get("company"));
        paramMap.addValue("department", map.get("department"));
        paramMap.addValue("address", map.get("address"));
        paramMap.addValue("type", type);
        paramMap.addValue("customerType", customerType);
        paramMap.addValue("state", User.STATE_VALID);
        return paramMap;
    }

    /**
     * @return
     * @author qiushaohua 2012-4-23
     */
    protected String createSalt() {
        return System.currentTimeMillis() + "";
    }

    @Override
    public OperateResult update(Map<String, Object> map) {
        String sql = "update sys_user set login_account = :loginAccount, name = :name, tel = :tel, short_number = :shortNumber, company_id = :companyId, "
                + " company = :company, department = :department, address = :address, customer_type = :customerType"
                + " where id = :id";

        User user = Converter.mapToBean(map, User.class);

        BeanPropertySqlParameterSource paramMap = new BeanPropertySqlParameterSource(user);
        OperateResult isUserUpdated = doUpdate(sql, paramMap);

        if (!isUserUpdated.isOk()) {
            return isUserUpdated;
        }

        if (user.getType() == Type.TYPE_SELF) {
            // update the scheduler info in processing order
            sql = "update dispatch_order set scheduler_name = :name, scheduler_tel = :tel"
                    + " where scheduler_id = :id and state < " + State.DELIVERED.ordinal();
            jdbcTemplate.update(sql, paramMap);

            // update the current handler info in processing order
            sql = "update dispatch_order set handler_name = :name, handler_tel = :tel"
                    + " where handler_id = :id and state < " + State.DELIVERED.ordinal();
            jdbcTemplate.update(sql, paramMap);

            // // update the schedule info in processing order
            // sql = "update dispatch_schedule_detail set handler_name = :name, handler_tel = :tel"
            // + " where handler_id = :id and order_id in (select id from dispatch_order where state < "
            // + State.DELIVERED.ordinal() + ")";
            // jdbcTemplate.update(sql, paramMap);
            //
            // // update the schedule info in processing order
            // sql = "update dispatch_handle_detail set handler_name = :name, handler_tel = :tel"
            // + " where handler_id = :id and order_id in (select id from dispatch_order where state < "
            // + State.DELIVERED.ordinal() + ")";
            // jdbcTemplate.update(sql, paramMap);
        } else {
            // update the sender info in processing order
            sql = "update dispatch_order set sender_name = :name, sender_tel = :tel, sender_company = :company, sender_address = :address"
                    + " where sender_id = :id and state < " + State.DELIVERED.ordinal();
            jdbcTemplate.update(sql, paramMap);
        }

        if (map.containsKey("roleId")) {
            return roleRepository.update(map);
        }

        return isUserUpdated;
    }

    /**
     * @param typeSelf
     * @return
     * @author qiushaohua 2012-5-4
     */
    public int getMaxCode(int type) {
        String sql = "select max(cast(code as int)) code from sys_user where type = :type";
        SqlParameterSource paramMap = new MapSqlParameterSource("type", type);
        int maxCode = jdbcTemplate.queryForInt(sql, paramMap);
        return maxCode;
    }

}
