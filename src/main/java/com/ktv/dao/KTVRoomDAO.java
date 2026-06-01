package com.ktv.dao;

import com.ktv.entity.KTVRoom;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class KTVRoomDAO extends BaseDAO {

    public boolean addRoom(KTVRoom r) {
        String sql = "INSERT INTO KTV房间 (房间号, 房间类型, 销量, 房间状态) VALUES (?,?,?,?)";
        return executeUpdate(sql, r.getRoomId(), r.getRoomType(), r.getSales(), r.getRoomStatus()) > 0;
    }

    public boolean updateRoom(KTVRoom r) {
        String sql = "UPDATE KTV房间 SET 房间类型=?, 销量=?, 房间状态=? WHERE 房间号=?";
        return executeUpdate(sql, r.getRoomType(), r.getSales(), r.getRoomStatus(), r.getRoomId()) > 0;
    }

    public boolean deleteRoom(String roomId) {
        return executeUpdate("DELETE FROM KTV房间 WHERE 房间号=?", roomId) > 0;
    }
    public boolean deleteBatch(List<String> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return executeUpdate("DELETE FROM KTV房间 WHERE 房间号 IN (" + qs + ")", ids.toArray()) > 0;
    }

    public KTVRoom getRoomByNo(String roomId) {
        return queryForObject("SELECT * FROM KTV房间 WHERE 房间号=?", new RowMapperImpl(), roomId);
    }


    public List<KTVRoom> getAllRooms() {
        String sql = "SELECT r.房间号, r.房间类型, r.销量, r.房间状态, " +
                "b.顾客账号, c.姓名 AS 顾客姓名, c.联系电话, " +
                "b.预约开始时间, b.预约结束时间 " +
                "FROM KTV房间 r " +
                "LEFT JOIN 预约单 b ON r.房间号 = b.房间号 " +
                "LEFT JOIN 顾客 c ON b.顾客账号 = c.顾客账号 " +
                "ORDER BY r.房间号,b.预约开始时间";
        return queryForList(sql, new RowMapperImpl());
    }


    public List<KTVRoom> getooms() {
        String sql = "SELECT r.房间号, r.房间类型, r.销量, r.房间状态, " +
                "b.顾客账号, c.姓名 AS 顾客姓名, c.联系电话, " +
                "b.预约开始时间, b.预约结束时间 " +
                "FROM KTV房间 r " +
                "inner JOIN 预约单 b ON r.房间号 = b.房间号 " +
                "inner JOIN 顾客 c ON b.顾客账号 = c.顾客账号 " +
                "ORDER BY r.房间号";
        return queryForList(sql, new RowMapperImpl());
    }
    public List<KTVRoom> getRoomsByStatus() {
        return queryForList("SELECT * FROM KTV房间", new RowMapperImpl());
    }

    public boolean updateRoomStatus(String roomId, String status) {
        return executeUpdate("UPDATE KTV房间 SET 房间状态=? WHERE 房间号=?", status, roomId) > 0;
    }

    /* -------- RowMapper -------- */
    private static class RowMapperImpl implements RowMapper<KTVRoom> {
        public KTVRoom mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            KTVRoom r = new KTVRoom();
            r.setRoomId(rs.getString("房间号"));
            r.setRoomType(rs.getString("房间类型"));
            r.setSales(rs.getInt("销量"));
            r.setRoomStatus(rs.getString("房间状态"));

            try {
                r.setCustomerId(rs.getString("顾客账号"));
                r.setCustomerName(rs.getString("顾客姓名"));
                r.setCustomerPhone(rs.getString("联系电话"));
                Timestamp start = rs.getTimestamp("预约开始时间");
                Timestamp end   = rs.getTimestamp("预约结束时间");
                r.setStartDate(start == null ? null : new Date(start.getTime()));
                r.setEndDate(end == null ? null : new Date(end.getTime()));
            }catch (Exception e){
                System.out.println(e.getMessage());
                return r;
            }

            return r;
        }
    }


}