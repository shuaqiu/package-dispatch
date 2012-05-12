/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.repository.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.customer.Receiver;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-3-31
 * @version 0.0.1
 */
@Repository
public class ReceiverRepository extends AbstractRepository implements ResourceRepository<Receiver> {

    /**
     * @author qiushaohua 2012-3-24
     * @version 0.0.1
     */
    private final class ReceiverRowMapper implements RowMapper<Receiver> {
        @Override
        public Receiver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Receiver user = new Receiver();
            user.setId(rs.getInt("id"));
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            user.setTel(rs.getString("tel"));
            user.setCompanyId(rs.getInt("company_id"));
            user.setCompany(rs.getString("company"));
            user.setAddress(rs.getString("address"));
            return user;
        }
    }

    /**
     * @param userId
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-3-31
     */
    public List<Receiver> query(int userId, String sort, String query, long[] range) {
        String sql = "select *, row_number() over(" + orderBy(sort) + ") as rownum"
                + " from customer_receiver where user_id = :userId";
        MapSqlParameterSource paramMap = new MapSqlParameterSource("userId", userId);

        if (StringUtils.hasText(query)) {
            sql += " and (name like :query or company like :query or address like :query)";
            paramMap.addValue("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        List<Receiver> list = jdbcTemplate.query(rangeQuerySql, paramMap, new ReceiverRowMapper());
        return list;
    }

    /**
     * @param userId
     * @param query
     * @return
     * @author qiushaohua 2012-4-4
     */
    public long matchedRecordCount(int userId, String query) {
        String sql = "select count(*) from customer_receiver receiver where receiver.user_id = :userId";
        MapSqlParameterSource paramMap = new MapSqlParameterSource("userId", userId);

        if (StringUtils.hasText(query)) {
            sql += " and (name like :query or company like :query or address like :query)";
            paramMap.addValue("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        return jdbcTemplate.queryForLong(sql, paramMap);
    }

    @Override
    public Receiver query(int id) {
        return doQuery("customer_receiver", id, new ReceiverRowMapper());
    }

    @Override
    public boolean delete(int id) {
        return doDelete("customer_receiver", id);
    }

    @Override
    public OperateResult insert(Receiver receiver) {
        String sql = "insert into customer_receiver(user_id, name, tel, company_id, company, address)"
                + " values(:userId, :name, :tel, :companyId, :company, :address)";

        return doInsert(sql, new BeanPropertySqlParameterSource(receiver));
    }

    @Override
    public OperateResult update(Receiver receiver) {
        String sql = "update customer_receiver set user_id = :userId, name = :name, tel = :tel,"
                + " company_id = :companyId, company = :company, address = :address where id = :id";

        return doUpdate(sql, new BeanPropertySqlParameterSource(receiver));
    }

}
