package com.ktv.dao;

import com.ktv.entity.Reservation;
import com.ktv.entity.RoomUsage;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RoomUsageDAO extends BaseDAO {

    public boolean insert(String account, String roomId, LocalDateTime start, LocalDateTime end) {
        String sql = "INSERT INTO 房间使用记录 (顾客账号, 房间号, 开始使用时间, 结束使用时间) VALUES (?,?,?,?)";
        return executeUpdate(sql, account, roomId, start, end) > 0;
    }

    public boolean endUsage(String roomId, LocalDateTime end) {
        String sql = "UPDATE 房间使用记录 SET 结束使用时间=? " +
                "WHERE 房间号=? AND 结束使用时间 IS NULL";
        return executeUpdate(sql, end, roomId) > 0;
    }
    public boolean updateUsage(Reservation r) {
        String sql = "UPDATE 预约单 SET 预约开始时间=?,预约结束时间=? WHERE 顾客账号=? AND 房间号=?";
        return executeUpdate(sql, r.getStartTime(), r.getEndTime(), r.getAccount(), r.getRoomId()) > 0;
    }
    public boolean deleteUsage(String account, String roomId) {
        return executeUpdate("DELETE FROM 预约单 WHERE 顾客账号=? AND 房间号= ?", account, roomId) > 0;
    }
    
    public boolean deleteRoomUsage(String account, String roomId) {
        return executeUpdate("DELETE FROM 房间使用记录 WHERE 顾客账号=? AND 房间号= ?", account, roomId) > 0;
    }
    public boolean deleteBatch(List<String> accounts, List<String> roomIds) {
        if (accounts.isEmpty()) return true;
        String qs = accounts.stream().map(i -> "(?,?)").collect(Collectors.joining(","));
        Object[] args = new Object[accounts.size() * 2];
        for (int i = 0; i < accounts.size(); i++) {
            args[i * 2] = accounts.get(i);
            args[i * 2 + 1] = roomIds.get(i);
        }
        return executeUpdate("DELETE FROM 预约单 WHERE (顾客账号,房间号) IN (" + qs + ")", args) > 0;
    }
    public RoomUsage selectActive(String roomId) {
        return queryForObject("SELECT * FROM 房间使用记录 " +
                "WHERE 房间号=? AND 结束使用时间 IS NULL", new RowMapperImpl(), roomId);
    }

    public RoomUsage getActive(String roomId) {
        return queryForObject("SELECT * FROM 房间使用记录 " +
                "WHERE 房间号=?", new RowMapperImpl(), roomId);
    }
    public boolean updateUsage(RoomUsage u) {
        String sql = "UPDATE 房间使用记录 SET 开始使用时间=?,结束使用时间=? WHERE 顾客账号=? AND 房间号=?";
        return executeUpdate(sql,
                u.getStartTime(), u.getEndTime(),
                u.getAccount(), u.getRoomId()) > 0;
    }

    public List<RoomUsage> selectByCustomer(String account) {
        return queryForList("SELECT * FROM 房间使用记录 WHERE 顾客账号=?", new RowMapperImpl(), account);
    }

    private static class RowMapperImpl implements RowMapper<RoomUsage> {
        public RoomUsage mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            RoomUsage u = new RoomUsage();
            u.setAccount(rs.getString("顾客账号"));
            u.setRoomId(rs.getString("房间号"));
            u.setStartTime(rs.getTimestamp("开始使用时间").toLocalDateTime());
            // 数据库 NULL 时返回 null，避免 NPE
            java.sql.Timestamp ts = rs.getTimestamp("结束使用时间");
            u.setEndTime(ts == null ? null : ts.toLocalDateTime());
            return u;
        }
    }
}