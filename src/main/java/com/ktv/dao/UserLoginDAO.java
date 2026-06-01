package com.ktv.dao;

import com.ktv.entity.UserLogin;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Repository
public class UserLoginDAO extends BaseDAO {

    public boolean addUser(UserLogin u) {
        String sql = "INSERT INTO 用户登录 (账号, 密码, 身份) VALUES (?,?,?)";
        return executeUpdate(sql, u.getAccount(), u.getPassword(), u.getRole()) > 0;
    }

    public boolean updateUser(UserLogin u) {
        String sql = "UPDATE 用户登录 SET 密码=?, 身份=? WHERE 账号=?";
        return executeUpdate(sql, u.getPassword(), u.getRole(), u.getAccount()) > 0;
    }

    public boolean deleteUser(String account) {
        return executeUpdate("DELETE FROM 用户登录 WHERE 账号=?", account) > 0;
    }

    public UserLogin getUserByAccount(String account) {
        return queryForObject("SELECT * FROM 用户登录 WHERE 账号=?", new RowMapperImpl(), account);
    }

    public List<UserLogin> getAllUsers() {
        return queryForList("SELECT * FROM 用户登录 ORDER BY 账号", new RowMapperImpl());
    }

    public List<UserLogin> getUsersByRole(String role) {
        return queryForList("SELECT * FROM 用户登录 WHERE 身份=?", new RowMapperImpl(), role);
    }

    public boolean exist(String account) {
        return queryCount("SELECT COUNT(*) FROM 用户登录 WHERE 账号=?", account) > 0;
    }

    public boolean changePassword(String account, String newPwd) {
        return executeUpdate("UPDATE 用户登录 SET 密码=? WHERE 账号=?", newPwd, account) > 0;
    }

    public boolean isValidAccount(String account) {
        return account != null && account.matches("^[a-zA-Z0-9_]{4,20}$");
    }

    public boolean isValidPassword(String pwd) {
        return pwd != null && pwd.length() >= 6 && pwd.length() <= 50;
    }

    private static class RowMapperImpl implements RowMapper<UserLogin> {
        public UserLogin mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            UserLogin u = new UserLogin();
            u.setAccount(rs.getString("账号"));
            u.setPassword(rs.getString("密码"));
            u.setRole(rs.getString("身份"));
            return u;
        }
    }
}