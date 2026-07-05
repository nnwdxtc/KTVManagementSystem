package com.ktv.dao;

import com.ktv.entity.Reservation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationDAO extends BaseDAO {

    /**
     * 插入预约记录
     */
    public boolean insert(Reservation reservation) {
        String sql = "INSERT INTO 预约单 (顾客账号, 房间号, 预约开始时间, 预约结束时间) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql,
                reservation.getAccount(),
                reservation.getRoomId(),
                reservation.getStartTime(),
                reservation.getEndTime()) > 0;
    }

    /**
     * 查询顾客的预约列表
     */
    public List<Reservation> listByCustomer(String customerId) {
        String sql = "SELECT * FROM 预约单 WHERE 顾客账号 = ? ORDER BY 预约开始时间 DESC";
        return queryForList(sql, new ReservationRowMapper(), customerId);
    }

    /**
     * 通过顾客ID和房间号删除预约
     */
    public boolean deleteByCustomerAndRoom(String customerId, String roomNo) {
        String sql = "DELETE FROM 预约单 WHERE 顾客账号 = ? AND 房间号 = ?";
        return executeUpdate(sql, customerId, roomNo) > 0;
    }

    /**
     * 通过预约单ID删除预约
     */
    public boolean deleteById(int reservationId) {
        String sql = "DELETE FROM 预约单 WHERE id = ?";
        return executeUpdate(sql, reservationId) > 0;
    }

    /**
     * 删除预约记录（通过顾客账号和房间号）
     */
    public boolean delete(String account, String roomNo) {
        String sql = "DELETE FROM 预约单 WHERE 顾客账号 = ? AND 房间号 = ?";
        return executeUpdate(sql, account, roomNo) > 0;
    }

    /**
     * 获取所有预约记录（服务员用）
     */
    public List<Reservation> getAllReservations() {
        String sql = "SELECT * FROM 预约单 ORDER BY 预约开始时间 DESC";
        return queryForList(sql, new ReservationRowMapper());
    }

    /**
     * 通过房间号获取预约记录列表
     */
    public List<Reservation> selectByRoom(String roomId) {
        String sql = "SELECT * FROM 预约单 WHERE 房间号 = ? ORDER BY 预约开始时间 DESC";
        return queryForList(sql, new ReservationRowMapper(), roomId);
    }

    /**
     * 通过房间号获取预约记录列表（别名方法）
     */
    public List<Reservation> getByRoomId(String roomId) {
        String sql = "SELECT * FROM 预约单 WHERE 房间号 = ? ORDER BY 预约开始时间 DESC";
        return queryForList(sql, new ReservationRowMapper(), roomId);
    }

    /**
     * 通过房间号查询预约记录（单个，返回最新一条）
     */
    public Reservation select(String roomNo) {
        String sql = "SELECT * FROM 预约单 WHERE 房间号 = ? ORDER BY 预约开始时间 DESC LIMIT 1";
        return queryForObject(sql, new ReservationRowMapper(), roomNo);
    }

    /**
     * 通过房间号和顾客账号查询预约记录
     */
    public Reservation selectByRoomAndCustomer(String roomNo, String customerId) {
        String sql = "SELECT * FROM 预约单 WHERE 房间号 = ? AND 顾客账号 = ? ORDER BY 预约开始时间 DESC LIMIT 1";
        return queryForObject(sql, new ReservationRowMapper(), roomNo, customerId);
    }

    /**
     * 检查房间在指定时间段是否已被预约
     */
    public boolean isRoomBooked(String roomId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT COUNT(1) FROM 预约单 " +
                "WHERE 房间号 = ? " +
                "AND ((预约开始时间 < ? AND 预约结束时间 > ?) " +
                "OR (预约开始时间 BETWEEN ? AND ?))";
        int count = queryCount(sql, roomId, endTime, startTime, startTime, endTime);
        return count > 0;
    }

    /**
     * 删除过期的预约记录
     */
    public boolean deleteExpired() {
        String sql = "DELETE FROM 预约单 WHERE 预约结束时间 < NOW()";
        return executeUpdate(sql) > 0;
    }

    /**
     * RowMapper 实现
     */
    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reservation reservation = new Reservation();
            reservation.setAccount(rs.getString("顾客账号"));
            reservation.setRoomId(rs.getString("房间号"));

            try {
                reservation.setStartTime(rs.getTimestamp("预约开始时间").toLocalDateTime());
            } catch (Exception e) {
                reservation.setStartTime(null);
            }
            try {
                reservation.setEndTime(rs.getTimestamp("预约结束时间").toLocalDateTime());
            } catch (Exception e) {
                reservation.setEndTime(null);
            }
            return reservation;
        }
    }
}