package com.ktv.dao;

import com.ktv.entity.Reservation;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationDAO extends BaseDAO {

    public boolean hasConflict(String roomId, LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT COUNT(*) FROM 预约单 " +
                "WHERE 房间号=? " +
                "AND NOT (预约结束时间 <= ? OR 预约开始时间 >= ?)";
        return queryCount(sql, roomId, start, end) > 0;
    }

    public boolean insert(Reservation r) {
        String sql = "INSERT INTO 预约单 (顾客账号, 房间号, 预约开始时间, 预约结束时间) VALUES (?,?,?,?)";
        return executeUpdate(sql, r.getAccount(), r.getRoomId(), r.getStartTime(), r.getEndTime()) > 0;
    }
    public boolean update(Reservation r) {
        String sql = "UPDATE 预约单 SET 预约开始时间=?,预约结束时间=? WHERE 顾客账号=? AND 房间号=?";
        return executeUpdate(sql, r.getStartTime(), r.getEndTime(), r.getAccount(), r.getRoomId()) > 0;
    }
    public boolean delete(String account, String roomId) {
        return executeUpdate("DELETE FROM 预约单 WHERE 顾客账号=? AND 房间号=?", account, roomId) > 0;
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
    public Reservation select(String roomId) {
        return queryForObject("SELECT * FROM 预约单 WHERE 房间号=?", new RowMapperImpl(), roomId);
    }

    public List<Reservation> selectByRoom(String roomId) {
        return queryForList("SELECT * FROM 预约单 WHERE 房间号=?", new RowMapperImpl(), roomId);
    }

    private static class RowMapperImpl implements RowMapper<Reservation> {
        public Reservation mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Reservation r = new Reservation();
            r.setAccount(rs.getString("顾客账号"));
            r.setRoomId(rs.getString("房间号"));
            r.setStartTime(rs.getTimestamp("预约开始时间").toLocalDateTime());
            r.setEndTime(rs.getTimestamp("预约结束时间").toLocalDateTime());
            return r;
        }
    }
    public List<Reservation> selectByCustomerAndRoom(String account, String roomId) {
        String sql = "SELECT * FROM 预约单 WHERE 顾客账号=? AND 房间号=?";
        return queryForList(sql, new RowMapperImpl(), account, roomId);
    }

    /**
     * 调用存储过程：获取顾客预约记录
     */
    public List<Reservation> selectByCustomerStoredProcedure(String customerId) {
        String sql = "CALL sp_获取顾客预约记录(?)";
        return queryForList(sql, new RowMapperImpl(), customerId);
    }
}