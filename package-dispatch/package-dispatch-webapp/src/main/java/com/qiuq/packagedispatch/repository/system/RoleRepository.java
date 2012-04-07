/**
 * @author qiushaohua 2012-4-8
 */
package com.qiuq.packagedispatch.repository.system;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.common.convert.Converter;
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
        if (StringUtils.hasText(query)) {
            sql += " and (code like :query or name like :query or address like :query)";
            paramMap.addValue("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        return sql;
    }

    @Override
    public Map<String, Object> query(int id) {
        return null;
    }

    @Override
    public boolean insert(Map<String, Object> t) {
        String sql = "insert into sys_user_role(user_id, role_id) values (:id, :role_id)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource(t);
        return doInsert(sql, paramMap);
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean update(int id, Map<String, Object> t) {
        String sql = "update sys_user_role set role_id = :role_id where user_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource(t);
        boolean doUpdate = doUpdate(sql, paramMap);
        if (!doUpdate) {
            return insert(t);
        }
        return doUpdate;
    }

}
