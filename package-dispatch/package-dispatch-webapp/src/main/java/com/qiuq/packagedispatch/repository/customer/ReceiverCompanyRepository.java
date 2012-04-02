/**
 * @author qiushaohua 2012-4-1
 */
package com.qiuq.packagedispatch.repository.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.qiuq.packagedispatch.bean.customer.ReceiverCompany;
import com.qiuq.packagedispatch.repository.AbstractRepository;
import com.qiuq.packagedispatch.repository.ResourceMapper;
import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-4-1
 * @version 0.0.1
 */
@Repository
public class ReceiverCompanyRepository extends AbstractRepository implements ResourceRepository<ReceiverCompany> {
    private final class ReceiverCompanyRowMapper implements RowMapper<ReceiverCompany> {
        @Override
        public ReceiverCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReceiverCompany com = new ReceiverCompany();
            com.setId(rs.getInt("id"));
            com.setUserId(rs.getInt("user_id"));
            com.setName(rs.getString("name"));
            com.setAddress(rs.getString("address"));
            return com;
        }
    }

    private final class ReceiverCompanyResourceMapper implements ResourceMapper<ReceiverCompany> {
        @Override
        public MapSqlParameterSource mapObject(ReceiverCompany t, SqlSourceType sourceType) {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue("userId", t.getUserId());
            paramMap.addValue("name", t.getName());
            paramMap.addValue("address", t.getAddress());
            return paramMap;
        }
    }

    /**
     * @param userId
     * @param sort
     * @param query
     * @param range
     * @return
     * @author qiushaohua 2012-4-1
     */
    public List<ReceiverCompany> query(int userId, String sort, String query, long[] range) {
        String sql = "select com.*, row_number() over(" + orderBy(sort) + ") as rownum"
                + " from customer_receiver_company com where com.user_id = :userId";

        MapSqlParameterSource paramMap = new MapSqlParameterSource("userId", userId);

        if (StringUtils.hasText(query)) {
            sql += " and (code like :query or name like :query or address like :query)";
            paramMap.addValue("query", "%" + sqlUtil.escapeLikeValue(query) + "%");
        }

        String rangeQuerySql = sqlUtil.toRangeQuerySql(sql, range);
        List<ReceiverCompany> list = jdbcTemplate.query(rangeQuerySql, paramMap, new ReceiverCompanyRowMapper());
        return list;
    }

    @Override
    public ReceiverCompany query(int id) {
        return doQuery("customer_receiver_company", id, new ReceiverCompanyRowMapper());
    }

    @Override
    public boolean delete(int id) {
        return doDelete("customer_receiver_company", id);
    }

    @Override
    public boolean insert(ReceiverCompany t) {
        String sql = "insert into customer_receiver_company(user_id, name, address) values(:userId, :name, :address)";
        return doInsert(sql, t, new ReceiverCompanyResourceMapper());
    }

    @Override
    public boolean update(int id, ReceiverCompany t) {
        String sql = "update customer_receiver_company set user_id = :userId, name = :name, address = :address where id = :id";
        return doUpdate(sql, id, t, new ReceiverCompanyResourceMapper());
    }

}
