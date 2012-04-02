/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.repository;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public interface SqlUtil {

    /**
     * @param likeValue
     * @return
     * @author qiushaohua 2012-3-27
     */
    String escapeLikeValue(String likeValue);

    /**
     * @param sql
     * @param range
     * @return
     * @author qiushaohua 2012-4-2
     */
    String toRangeQuerySql(String sql, long[] range);
}
