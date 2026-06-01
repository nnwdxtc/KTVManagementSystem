package com.ktv.dao;

import com.ktv.entity.SongOrder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Repository
public class SongOrderDAO extends BaseDAO {

    public boolean insert(SongOrder o) {
        String sql = "INSERT INTO 点播记录 (顾客账号, 歌曲编号, 点播次数) VALUES (?,?,1) " +
                "ON DUPLICATE KEY UPDATE 点播次数=点播次数+1";
        return executeUpdate(sql, o.getAccount(), o.getSongId()) > 0;
    }

    public boolean delete(String customerId, int songId) {
        return executeUpdate(
                "DELETE FROM 点播记录 WHERE 顾客账号 = ? AND 歌曲编号 = ?",
                customerId, songId
        ) > 0;
    }

    public SongOrder getById(int orderId) {
        return queryForObject("SELECT * FROM 点播记录 WHERE 点播ID=?", new RowMapperImpl(), orderId);
    }

    public List<SongOrder> listByCustomer(String account) {
        String sql = "SELECT o.顾客账号, o.歌曲编号, o.点播次数, " +
                "       s.歌名, s.歌手 " +
                "FROM 点播记录 o " +
                "JOIN 歌曲 s ON o.歌曲编号 = s.歌曲编号 " +
                "WHERE o.顾客账号 = ?";
        return queryForList(sql, new RowMapperImpl(), account);
    }

    public List<SongOrder> listBySong(int songId) {
        return queryForList("SELECT * FROM 点播记录 WHERE 歌曲编号=?", new RowMapperImpl(), songId);
    }

    private static class RowMapperImpl implements RowMapper<SongOrder> {
        public SongOrder mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            SongOrder o = new SongOrder();
            o.setAccount(rs.getString("顾客账号"));
            o.setSongId(rs.getInt("歌曲编号"));
            o.setPlayCount(rs.getInt("点播次数"));
            o.setSongName(rs.getString("歌名"));
            o.setSinger(rs.getString("歌手"));
            return o;
        }
    }
}