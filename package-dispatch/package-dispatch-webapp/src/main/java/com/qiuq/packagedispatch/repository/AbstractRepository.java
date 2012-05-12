/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public abstract class AbstractRepository {

    protected NamedParameterJdbcTemplate jdbcTemplate;
    protected SqlUtil sqlUtil;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** @author qiushaohua 2012-3-27 */
    @Autowired
    public void setSqlUtil(SqlUtil sqlUtil) {
        this.sqlUtil = sqlUtil;
    }

    /**
     * @param sort
     * @param sql
     * @return
     * @author qiushaohua 2012-3-27
     */
    protected String orderBy(String sort) {
        if (StringUtils.hasText(sort) && sort.length() > 1) {
            if (sort.startsWith("+") || sort.startsWith(" ")) {
                return " order by " + underscoreName(sort.substring(1));
            } else if (sort.startsWith("-")) {
                return " order by " + underscoreName(sort.substring(1)) + " desc";
            }
        }
        return "";
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case. Any upper case letters are converted to lower
     * case with a preceding underscore.
     * 
     * @param name
     *            the string containing original name
     * @return the converted name
     * @see BeanPropertyRowMap
     */
    private String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase()) && s.matches("[A-Z]")) {
                    result.append("_");
                    result.append(s.toLowerCase());
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }

    /**
     * @param table
     * @param id
     * @param rowMapper
     * @return
     * @author qiushaohua 2012-3-31
     */
    protected <T> T doQuery(String table, int id, RowMapper<T> rowMapper) {
        String sql = "select * from " + table + " where id = :id";
        SqlParameterSource paramMap = new MapSqlParameterSource("id", id);

        try {
            T t = jdbcTemplate.queryForObject(sql, paramMap, rowMapper);
            return t;
        } catch (DataAccessException e) {
        }
        return null;
    }

    /**
     * @param table
     * @param id
     * @return
     * @author qiushaohua 2012-3-31
     */
    protected boolean doDelete(String table, int id) {
        String sql = "delete from " + table + " where id = :id";
        SqlParameterSource paramMap = new MapSqlParameterSource("id", id);

        try {
            int update = jdbcTemplate.update(sql, paramMap);
            return update == 1;
        } catch (DataAccessException e) {
        }

        return false;
    }

    /**
     * @param sql
     * @param paramSource
     * @return
     * @author qiushaohua 2012-4-4
     */
    protected OperateResult doInsert(String sql, SqlParameterSource paramSource) {
        try {
            int inserted = jdbcTemplate.update(sql, paramSource);
            if (inserted == 1) {
                return OperateResult.OK;
            }
            return new OperateResult(ErrCode.INSERT_FAIL, "inserted row is not equals 1");
        } catch (DataAccessException e) {
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * @param sql
     * @param paramSource
     * @param generatedKeyHolder
     * @return
     * @author qiushaohua 2012-5-2
     */
    protected OperateResult doInsert(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder) {
        try {
            int inserted = jdbcTemplate.update(sql, paramSource, generatedKeyHolder);
            if (inserted == 1) {
                return new OperateResult(true, generatedKeyHolder);
            }
            return new OperateResult(ErrCode.INSERT_FAIL, "inserted row is not equals 1");
        } catch (DataAccessException e) {
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * @param sql
     * @param paramSource
     * @return
     * @author qiushaohua 2012-4-4
     */
    protected OperateResult doUpdate(String sql, SqlParameterSource paramSource) {
        try {
            int updated = jdbcTemplate.update(sql, paramSource);
            if (updated == 1) {
                return OperateResult.OK;
            }
            return new OperateResult(ErrCode.UPDATE_FAIL, "update row is not equals 1");
        } catch (DataAccessException e) {
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage(), e);
        }
    }
}
