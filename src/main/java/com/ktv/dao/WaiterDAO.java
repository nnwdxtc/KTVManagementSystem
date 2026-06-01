package com.ktv.dao;

import com.ktv.entity.Waiter;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class WaiterDAO extends BaseDAO {

    public boolean insert(Waiter w) {
        String sql = "INSERT INTO 服务员 (工号, 姓名, 性别, 电话) VALUES (?,?,?,?)";
        return executeUpdate(sql, w.getWaiterId(), w.getName(), w.getGender(), w.getPhone()) > 0;
    }

    public boolean update(Waiter w) {
        String sql = "UPDATE 服务员 SET 姓名=?, 性别=?, 电话=? WHERE 工号=?";
        return executeUpdate(sql, w.getName(), w.getGender(), w.getPhone(), w.getWaiterId()) > 0;
    }

    public boolean delete(String waiterId) {
        return executeUpdate("DELETE FROM 服务员 WHERE 工号=?", waiterId) > 0;
    }
    public boolean deleteBatch(List<String> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return executeUpdate("DELETE FROM 服务员 WHERE 工号 IN (" + qs + ")", ids.toArray()) > 0;
    }

    public Waiter getById(String waiterId) {
        return queryForObject("SELECT * FROM 服务员 WHERE 工号=?", new RowMapperImpl(), waiterId);
    }

    public List<Waiter> getAll() {
        return queryForList("SELECT * FROM 服务员 ORDER BY 工号", new RowMapperImpl());
    }

    public List<Waiter> getByGender(String gender) {
        return queryForList("SELECT * FROM 服务员 WHERE 性别=?", new RowMapperImpl(), gender);
    }

    public List<Waiter> getByName(String name) {
        return queryForList("SELECT * FROM 服务员 WHERE 姓名 LIKE ?", new RowMapperImpl(), "%" + name + "%");
    }

    public int count() {
        return queryCount("SELECT COUNT(*) FROM 服务员");
    }

    public static class RowMapperImpl implements RowMapper<Waiter> {
        public Waiter mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Waiter w = new Waiter();
            w.setWaiterId(rs.getString("工号"));
            w.setName(rs.getString("姓名"));
            w.setGender(rs.getString("性别"));
            w.setPhone(rs.getString("电话"));
            return w;
        }
    }
}