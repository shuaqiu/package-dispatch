/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * @author qiushaohua 2012-3-31
 * @version 0.0.1
 */
public interface ResourceMapper<T> {

    enum SqlSourceType {
        INSERT, UPDATE;
    }

    MapSqlParameterSource mapObject(T t, SqlSourceType sourceType);
}
