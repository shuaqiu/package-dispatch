/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public class AbstractRepository {

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
                return " order by " + sort.substring(1);
            } else if (sort.startsWith("-")) {
                return " order by " + sort.substring(1) + " desc";
            }
        }
        return "";
    }

}
