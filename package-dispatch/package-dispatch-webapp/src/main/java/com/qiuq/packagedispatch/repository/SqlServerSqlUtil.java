/**
 * @author qiushaohua 2012-3-26
 */
package com.qiuq.packagedispatch.repository;

/**
 * @author qiushaohua 2012-3-26
 * @version 0.0.1
 */
public class SqlServerSqlUtil {

    public static String escapeLikeCondition(String likeValue) {
        likeValue = likeValue.replaceAll("'", "''");
        likeValue = likeValue.replaceAll("\\[", "[[]");
        likeValue = likeValue.replaceAll("%", "[%]");
        likeValue = likeValue.replaceAll("\\_", "[_]");
        return likeValue;
    }
}
