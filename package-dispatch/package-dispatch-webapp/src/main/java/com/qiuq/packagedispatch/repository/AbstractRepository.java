/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import com.qiuq.common.convert.Converter;

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
    protected OperateResult doDelete(String table, int id) {
        String sql = "delete from " + table + " where id = :id";
        SqlParameterSource paramMap = new MapSqlParameterSource("id", id);

        try {
            int update = jdbcTemplate.update(sql, paramMap);
            if (update == 1) {
                return OperateResult.OK;
            }
            return new OperateResult(ErrCode.INSERT_FAIL, "delete row is not equals 1");
        } catch (DataAccessException e) {
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
        }
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
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
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
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
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
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
        }
    }

    /**
     * @param params
     * @param key
     * @param paramMap
     * @return <span style="font-weight: bold;">" and params.get(key + "Formula") params.get(key + "Op") :key"</span><br/>
     *         or <span style="font-weight: bold;">" and table.key params.get(key + "Op") :key"</span>
     * @author qiushaohua 2012-5-13
     */
    protected String buildIntCondition(Map<String, Object> params, String key, MapSqlParameterSource paramMap) {
        return buildIntCondition(params, key, null, paramMap);
    }

    /**
     * @param params
     * @param key
     * @param table
     * @param paramMap
     * @return <span style="font-weight: bold;">" and params.get(key + "Formula") params.get(key + "Op") :key"</span><br/>
     *         or <span style="font-weight: bold;">" and table.key params.get(key + "Op") :key"</span>
     * @author qiushaohua 2012-5-14
     */
    protected String buildIntCondition(Map<String, Object> params, String key, String table,
            MapSqlParameterSource paramMap) {
        int value = Converter.toInt(params.get(key), -1);
        if (value == -1) {
            return "";
        }

        paramMap.addValue(key, value);

        String column;
        String formula = Converter.toString(params.get(key + "Formula"));
        if (StringUtils.hasText(formula)) {
            // here, use the formula directly, not append the table.
            // enhance, the formula should set the table correctly.s
            column = formula;
        } else {
            column = buildColumn(table, key);
        }
        String op = Converter.toString(params.get(key + "Op"), "=").toLowerCase();
        // " and table.key op :key"
        String sql = " and " + column + " " + op + " :" + key;
        return sql;
    }

    /**
     * @param params
     * @param key
     * @param paramMap
     * @return " and key params.get(key + "Op") :key"
     * @author qiushaohua 2012-5-12
     */
    protected String buildStringCondition(Map<String, Object> params, String key, MapSqlParameterSource paramMap) {
        return buildStringCondition(params, key, null, paramMap);
    }

    /**
     * @param params
     * @param key
     * @param table
     * @param paramMap
     * @return " and table.key params.get(key + "Op") :key"
     * @author qiushaohua 2012-5-14
     */
    protected String buildStringCondition(Map<String, Object> params, String key, String table,
            MapSqlParameterSource paramMap) {
        String value = Converter.toString(params.get(key));
        if (!StringUtils.hasText(value)) {
            return "";
        }

        String op = Converter.toString(params.get(key + "Op"), "like").toLowerCase();
        if (op.equals("like") || op.equals("not like")) {
            paramMap.addValue(key, "%" + sqlUtil.escapeLikeValue(value) + "%");
        } else {
            paramMap.addValue(key, value);
        }

        // " and table.key op :key"
        String sql = " and " + buildColumn(table, key) + " " + op + " :" + key;
        return sql;
    }

    /**
     * @param params
     * @param key
     * @param paramMap
     * @return
     * @author qiushaohua 2011-6-1
     */
    protected String buildCollectionCondition(Map<String, Object> params, String key, MapSqlParameterSource paramMap) {
        return buildCollectionCondition(params, key, null, paramMap);
    }

    /**
     * @param params
     * @param key
     * @param table
     * @param paramMap
     * @return
     * @author qiushaohua 2011-6-1
     */
    protected String buildCollectionCondition(Map<String, Object> params, String key, String table,
            MapSqlParameterSource paramMap) {
        Object object = params.get(key);
        if (object == null) {
            return "";
        }

        if (!(object instanceof Collection)) {
            return buildCondition(params, key, table, paramMap);
        }

        Collection<?> collection = (Collection<?>) object;
        if (collection.size() == 0) {
            return "";
        }
        if (collection.size() == 1) {
            Object next = collection.iterator().next();
            params.put(key, next);
            return buildCondition(params, key, table, paramMap);
        }

        int count = 0;
        for (Object obj : collection) {
            if (obj != null) {
                count++;
                paramMap.addValue(key + count, obj);
            }
        }

        String op = Converter.toString(params.get(key + "Op"), "in").toLowerCase();
        StringBuilder sql = new StringBuilder(" and " + buildColumn(table, key) + " " + op + " (");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(":" + key + count);
        }
        sql.append(")");

        // " and table.key op :key"
        // String sql = " and " + buildColumn(table, key) + " " + op + " :" + key;
        return sql.toString();
    }

    /**
     * @param params
     * @param key
     * @param paramMap
     * @return
     * @author qiushaohua 2011-6-1
     */
    protected String buildCondition(Map<String, Object> params, String key, MapSqlParameterSource paramMap) {
        return buildCondition(params, key, null, paramMap);
    }

    /**
     * @param params
     * @param key
     * @param table
     * @param paramMap
     * @return
     * @author qiushaohua 2011-6-1
     */
    protected String buildCondition(Map<String, Object> params, String key, String table, MapSqlParameterSource paramMap) {
        Object object = params.get(key);
        if (object == null) {
            return "";
        }

        if (object instanceof String) {
            return buildStringCondition(params, key, table, paramMap);
        }
        if (object instanceof Integer) {
            return buildIntCondition(params, key, table, paramMap);
        }
        if (object instanceof Collection<?>) {
            return buildCollectionCondition(params, key, table, paramMap);
        }
        return "";
    }

    /**
     * @param table
     * @param key
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected String buildColumn(String table, String key) {
        String col = "";
        if (StringUtils.hasText(table)) {
            col += table;
            if (!table.endsWith(".")) {
                col += ".";
            }
        }
        col += underscoreName(key);
        return col;
    }

    /**
     * @param params
     * @param key
     * @param paramMap
     * @return
     * @author qiushaohua 2012-5-13
     */
    protected String buildQueryCondition(String query, MapSqlParameterSource paramMap, String...fields) {
        return buildQueryCondition(query, paramMap, null, fields);
    }

    /**
     * @param query
     * @param paramMap
     * @param fieldCondition
     * @param fields
     * @return
     * @author qiushaohua 2012-5-19
     */
    protected String buildQueryCondition(String query, MapSqlParameterSource paramMap,
            Map<String, String> fieldCondition, String... fields) {
        if (fields == null || fields.length == 0 || !StringUtils.hasText(query)) {
            return "";
        }

        paramMap.addValue("query", "%" + sqlUtil.escapeLikeValue(query) + "%");

        // and (field0 like :query or field1 like :query ...)
        StringBuilder sql = new StringBuilder();
        sql.append(" and (");
        List<String> s = new ArrayList<String>();
        for (String field : fields) {
            if (fieldCondition != null && StringUtils.hasText(fieldCondition.get(field))) {
                s.add("(" + field + " like :query and " + fieldCondition.get(field) + ")");
            } else {
                s.add(field + " like :query");
            }
        }
        sql.append(StringUtils.collectionToDelimitedString(s, " or "));
        sql.append(" )");

        return sql.toString();
    }

}
