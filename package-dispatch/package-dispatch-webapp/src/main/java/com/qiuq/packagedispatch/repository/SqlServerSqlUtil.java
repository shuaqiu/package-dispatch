/**
 * @author qiushaohua 2012-3-26
 */
package com.qiuq.packagedispatch.repository;

import org.springframework.stereotype.Component;

/**
 * @author qiushaohua 2012-3-26
 * @version 0.0.1
 */
@Component
public class SqlServerSqlUtil implements SqlUtil {

    @Override
    public String escapeLikeValue(String likeValue) {
        likeValue = likeValue.replaceAll("'", "''");
        likeValue = likeValue.replaceAll("\\[", "[[]");
        likeValue = likeValue.replaceAll("%", "[%]");
        likeValue = likeValue.replaceAll("\\_", "[_]");
        return likeValue;
    }

    /**
     * @param sql
     * @param range
     * @return
     * @author qiushaohua 2012-4-2
     */
    @Override
    public String toRangeQuerySql(String sql, long[] range) {
        if (range != null && range.length >= 2) {
            // sql = "select t.* from (" + sql + ") t where t.rownum between " + range[0] + " and " + range[1];
            sql = "with t as (" + sql + ") select t.* from t where t.rownum between " + range[0] + " and " + range[1];
        }
        return sql;
    }

    @Override
    public String getPinyinFunction() {
        return "dbo.fun_getPY";
    }
}
