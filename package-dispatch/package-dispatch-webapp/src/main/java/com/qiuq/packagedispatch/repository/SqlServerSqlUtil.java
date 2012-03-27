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
}
