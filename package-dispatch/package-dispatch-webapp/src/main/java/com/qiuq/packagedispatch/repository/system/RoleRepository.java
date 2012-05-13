/**
 * @author qiushaohua 2012-4-8
 */
package com.qiuq.packagedispatch.repository.system;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.system.Function;
import com.qiuq.packagedispatch.bean.system.Role;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-4-8
 * @version 0.0.1
 */
@Repository
public class RoleRepository extends AbstractRepository implements ResourceRepository<Map<String, Object>> {

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-3-27
     */
    @Override
    public List<Map<String, Object>> query(String sort, Map<String, Object> params, long[] range) {
        String sql = "select usr.*, role.role_id, row_number() over(" + orderBy(sort) + ") as rownum"
                + " from sys_user usr left join sys_user_role role on usr.id = role.user_id"
                + " where usr.id > 0 and usr.state = " + User.STATE_VALID + " and usr.type = " + Type.TYPE_SELF;
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        sql += buildCondition(params, paramMap);

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        return jdbcTemplate.query(rangeQuerySql, paramMap, new ColumnMapRowMapper());
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    public long matchedRecordCount(Map<String, Object> params) {
        String sql = "select count(*) from sys_user where id > 0 and state = " + User.STATE_VALID + " and type = "
                + Type.TYPE_SELF;
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

        String query = Converter.toString(params.get("query"));
        sql += buildQueryCondition(query, paramMap, "code", "login_account", "name", "address");

        return sql;
    }

    /**
     * @param user
     * @return
     * @author qiushaohua 2012-4-12
     */
    public List<Function> getAccessableFunctions(User user) {
        String sql = "select func.* from sys_function func"
                + " left join sys_role_function roleFunc on func.id = roleFunc.func_id"
                + " left join sys_role role on roleFunc.role_id = role.id"
                + " left join sys_user_role userRole on role.id = userRole.role_id"
                + " where userRole.user_id = :userId";
        SqlParameterSource paramMap = new MapSqlParameterSource("userId", user.getId());
        return jdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Function.class));
    }

    @Override
    public Map<String, Object> query(int id) {
        throw new UnsupportedOperationException("select a role by id is not supported");
    }

    @Override
    public OperateResult delete(int id) {
        throw new UnsupportedOperationException("delete a role by id is not supported");
    }

    @Override
    public OperateResult insert(Map<String, Object> t) {
        String sql = "insert into sys_user_role(user_id, role_id) values (:id, :roleId)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource(t);
        return doInsert(sql, paramMap);
    }

    @Override
    public OperateResult update(Map<String, Object> t) {
        String sql = "update sys_user_role set role_id = :roleId where user_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource(t);
        OperateResult doUpdate = doUpdate(sql, paramMap);
        if (!doUpdate.isOk()) {
            return insert(t);
        }
        return doUpdate;
    }

    /**
     * @param user
     * @return
     * @author qiushaohua 2012-5-7
     */
    public List<Role> getUserRoles(User user) {
        String sql = "select role.* from sys_role role left join sys_user_role userRole on role.id = userRole.role_id"
                + " where userRole.user_id = :userId";

        MapSqlParameterSource paramMap = new MapSqlParameterSource("userId", user.getId());
        return jdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Role.class));
    }

}
