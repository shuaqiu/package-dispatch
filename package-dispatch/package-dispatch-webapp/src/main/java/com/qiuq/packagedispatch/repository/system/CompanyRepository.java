/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.repository.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.qiuq.packagedispatch.bean.system.Company;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Repository
public class CompanyRepository {

    /**
     * @author qiushaohua 2012-3-24
     * @version 0.0.1
     */
    private final class CompanyRowMapper implements RowMapper<Company> {
        public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company com = new Company();

            com.setId(rs.getInt("id"));
            com.setCode(rs.getString("code"));
            com.setName(rs.getString("name"));
            com.setAddress(rs.getString("address"));
            com.setParentId(rs.getInt("parent_id"));
            com.setFullId(rs.getString("full_id"));
            com.setType(rs.getInt("type"));

            return com;
        }
    }

    private NamedParameterJdbcTemplate jdbcTemplate;

    /** @author qiushaohua 2012-3-24 */
    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @return
     * @author qiushaohua 2012-3-24
     */
    public List<Company> getCustomerCompanys() {
        String sql = "select * from sys_company where id > 0";

        SqlParameterSource paramMap = null;
        List<Company> companyList = jdbcTemplate.query(sql, paramMap, new CompanyRowMapper());

        return companyList;
    }

    /**
     * @param userId
     * @return
     * @author qiushaohua 2012-3-24
     */
    public List<Company> getReceiverCompanys(int userId) {

        String sql = "select com.* from sys_company com" + " left join sys_user user on com.id = user.company_id"
                + " left join sys_sender_receiver relation on user.id = relation.receiver_id"
                + " where relation.sender_id = :senderId";

        SqlParameterSource paramMap = new MapSqlParameterSource("senderId", userId);
        List<Company> companyList = jdbcTemplate.query(sql, paramMap, new CompanyRowMapper());
        return companyList;
    }

}
