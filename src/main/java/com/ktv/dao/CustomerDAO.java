package com.ktv.dao;

import com.ktv.common.R;
import com.ktv.entity.Customer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomerDAO extends BaseDAO {   // 1. 去掉 <Customer>
    // 2. 业务方法一字不改
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO 顾客 (顾客账号, 姓名, 性别, 联系电话) VALUES (?,?,?,?)";
        return executeUpdate(sql, c.getAccount(), c.getName(), c.getGender(), c.getPhone()) > 0;
    }

    public Customer getCustomerById(String account) {
        return queryForObject("SELECT * FROM 顾客 WHERE 顾客账号=?", new CustomerRowMapper(), account);
    }

    public List<Customer> getAllCustomers() {
        return queryForList("SELECT * FROM 顾客 ORDER BY 顾客账号", new CustomerRowMapper());
    }
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE 顾客 SET 姓名=?, 性别=?, 联系电话=? WHERE 顾客账号=?";
        return executeUpdate(sql, c.getName(), c.getGender(), c.getPhone(), c.getAccount()) > 0;
    }
    /* 其余方法完全保留，只把 ResultSetHandler 换成 RowMapper */
    public static class CustomerRowMapper implements RowMapper<Customer> {
        public Customer mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Customer c = new Customer();
            c.setAccount(rs.getString("顾客账号"));
            c.setName(rs.getString("姓名"));
            c.setGender(rs.getString("性别"));
            c.setPhone(rs.getString("联系电话"));
            return c;
        }
    }
    /* 追加到 CustomerDAO 最下面 */
    public boolean deleteCustomer(String account) {
        return executeUpdate("DELETE FROM 顾客 WHERE 顾客账号=?", account) > 0;
    }
    public boolean deleteBatch(List<String> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return executeUpdate("DELETE FROM 顾客 WHERE 顾客账号 IN (" + qs + ")", ids.toArray()) > 0;
    }

    public List<Customer> listByKeyword(String keyword) {
        return queryForList(
                "SELECT * FROM 顾客 WHERE 姓名 LIKE ? OR 顾客账号 LIKE ? ORDER BY 顾客账号",
                new CustomerRowMapper(),
                "%" + (keyword == null ? "" : keyword) + "%",
                "%" + (keyword == null ? "" : keyword) + "%");
    }
}