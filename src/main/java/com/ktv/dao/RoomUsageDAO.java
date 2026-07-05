package com.ktv.dao;

import com.ktv.entity.RoomUsage;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RoomUsageDAO extends BaseDAO {

    /**
     * ✅ 插入使用记录（通过顾客账号、房间号、开始时间、结束时间）
     */
    public boolean insert(String account, String roomNo, LocalDateTime start, LocalDateTime end) {
        String sql = "INSERT INTO 房间使用记录 (顾客账号, 房间号, 开始使用时间, 结束使用时间) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql, account, roomNo, start, end) > 0;
    }

    /**
     * 插入使用记录（通过 RoomUsage 对象）
     */
    public boolean insert(RoomUsage usage) {
        String sql = "INSERT INTO 房间使用记录 (顾客账号, 房间号, 开始使用时间, 结束使用时间) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql,
                usage.getAccount(),
                usage.getRoomId(),
                usage.getStartTime(),
                usage.getEndTime()) > 0;
    }

    /**
     * ✅ 获取当前正在使用的房间记录（结束使用时间为null表示使用中）
     */
    public RoomUsage getActive(String roomNo) {
        String sql = "SELECT * FROM 房间使用记录 WHERE 房间号 = ? AND 结束使用时间 IS NULL ORDER BY 开始使用时间 DESC LIMIT 1";
        return queryForObject(sql, new RoomUsageRowMapper(), roomNo);
    }

    /**
     * ✅ 获取当前正在使用的房间记录（别名方法）
     */
    public RoomUsage selectActive(String roomNo) {
        String sql = "SELECT * FROM 房间使用记录 WHERE 房间号 = ? AND 结束使用时间 IS NULL ORDER BY 开始使用时间 DESC LIMIT 1";
        return queryForObject(sql, new RoomUsageRowMapper(), roomNo);
    }

    /**
     * ✅ 结束使用（更新结束时间）
     */
    public boolean endUsage(String roomNo, LocalDateTime endTime) {
        String sql = "UPDATE 房间使用记录 SET 结束使用时间 = ? WHERE 房间号 = ? AND 结束使用时间 IS NULL";
        return executeUpdate(sql, endTime, roomNo) > 0;
    }

    /**
     * ✅ 删除使用记录
     */
    public boolean deleteRoomUsage(String account, String roomNo) {
        String sql = "DELETE FROM 房间使用记录 WHERE 顾客账号 = ? AND 房间号 = ?";
        return executeUpdate(sql, account, roomNo) > 0;
    }

    /**
     * ✅ 删除使用记录（别名方法）
     */
    public boolean deleteUsage(String account, String roomId) {
        String sql = "DELETE FROM 房间使用记录 WHERE 顾客账号 = ? AND 房间号 = ?";
        return executeUpdate(sql, account, roomId) > 0;
    }

    /**
     * ✅ 批量删除使用记录
     */
    public boolean deleteBatch(List<String> accounts, List<String> roomIds) {
        if (accounts.isEmpty() || roomIds.isEmpty()) return true;
        // 简单实现：逐条删除
        boolean success = true;
        for (int i = 0; i < accounts.size(); i++) {
            if (!deleteUsage(accounts.get(i), roomIds.get(i))) {
                success = false;
            }
        }
        return success;
    }

    /**
     * ✅ 更新使用记录
     */
    public boolean updateUsage(RoomUsage u) {
        String sql = "UPDATE 房间使用记录 SET 开始使用时间 = ?, 结束使用时间 = ? WHERE 顾客账号 = ? AND 房间号 = ?";
        return executeUpdate(sql, u.getStartTime(), u.getEndTime(), u.getAccount(), u.getRoomId()) > 0;
    }

    /**
     * 获取所有使用记录
     */
    public List<RoomUsage> getAll() {
        String sql = "SELECT * FROM 房间使用记录 ORDER BY 开始使用时间 DESC";
        return queryForList(sql, new RoomUsageRowMapper());
    }

    /**
     * 通过房间号获取使用记录
     */
    public List<RoomUsage> selectByRoom(String roomId) {
        String sql = "SELECT * FROM 房间使用记录 WHERE 房间号 = ? ORDER BY 开始使用时间 DESC";
        return queryForList(sql, new RoomUsageRowMapper(), roomId);
    }

    /**
     * RowMapper 实现
     */
    private static class RoomUsageRowMapper implements RowMapper<RoomUsage> {
        @Override
        public RoomUsage mapRow(ResultSet rs, int rowNum) throws SQLException {
            RoomUsage usage = new RoomUsage();
            usage.setAccount(rs.getString("顾客账号"));
            usage.setRoomId(rs.getString("房间号"));
            try {
                usage.setStartTime(rs.getTimestamp("开始使用时间").toLocalDateTime());
            } catch (Exception e) {
                usage.setStartTime(null);
            }
            try {
                usage.setEndTime(rs.getTimestamp("结束使用时间").toLocalDateTime());
            } catch (Exception e) {
                usage.setEndTime(null);
            }
            return usage;
        }
    }
}