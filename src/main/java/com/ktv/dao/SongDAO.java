package com.ktv.dao;

import com.ktv.entity.Song;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SongDAO extends BaseDAO {

    public boolean increasePlayCount(String customerId,int songId) {
        String sql = "UPDATE 点播记录 SET 点播次数 = 点播次数 + 1 WHERE 顾客账号 = ? AND 歌曲编号 = ?";
        return executeUpdate(sql,customerId, songId) > 0;
    }

    public boolean addSong(Song s) {
        String sql = "INSERT INTO 歌曲 (歌名, 歌手) VALUES (?,?)";
        return executeUpdate(sql, s.getTitle(), s.getArtist()) > 0;
    }

    public boolean updateSong(Song s) {
        String sql = "UPDATE 歌曲 SET 歌名=?, 歌手=? WHERE 歌曲编号=?";
        return executeUpdate(sql, s.getTitle(), s.getArtist(), s.getSongId()) > 0;
    }

    public boolean deleteSong(int songId) {
        return executeUpdate("DELETE FROM 歌曲 WHERE 歌曲编号=?", songId) > 0;
    }

    public Song getSongById(int songId) {
        return queryForObject("SELECT * FROM 歌曲 WHERE 歌曲编号=?", new RowMapperImpl(), songId);
    }
    public boolean deleteBatch(List<Integer> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return executeUpdate("DELETE FROM 歌曲 WHERE 歌曲编号 IN (" + qs + ")", ids.toArray()) > 0;
    }

    public List<Song> getAllSongs() {
        return queryForList("SELECT * FROM 歌曲 ORDER BY 歌名", new RowMapperImpl());
    }

    public List<Song> getSongsByName(String keyword) {
        return queryForList("SELECT * FROM 歌曲 WHERE 歌名 LIKE ?", new RowMapperImpl(), "%" + keyword + "%");
    }



    public List<Song> getHotSongs() {
        String sql = "SELECT 歌曲编号, 歌名, 歌手, 总点播次数 AS playCount " +
                "FROM vw_歌曲点播统计 " +
                "ORDER BY 总点播次数 DESC, 歌曲编号";
        return queryForList(sql, new HotRowMapper());
    }

    public int getTotalPlayCount(int songId) {
        return queryCount("SELECT SUM(点播次数) FROM 点播记录 WHERE 歌曲编号=?", songId);
    }

    /* 分页关键字查询 */
    public Page<Song> page(Page<Song> p, String keyword) {
        String sql = "SELECT * FROM 歌曲 " +
                "WHERE 歌名 LIKE ? OR 歌手 LIKE ? " +
                "ORDER BY 歌曲编号 DESC " +
                "LIMIT ?,?";
        String kw = "%" + (keyword == null ? "" : keyword) + "%";
        List<Song> records = queryForList(sql, new RowMapperImpl(), kw, kw, p.offset(), p.getSize());
        long total = queryCount("SELECT COUNT(1) FROM 歌曲 WHERE 歌名 LIKE ? OR 歌手 LIKE ?", kw, kw);
        p.setRecords(records);
        p.setTotal(total);
        return p;
    }


    private static class RowMapperImpl implements RowMapper<Song> {
        public Song mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Song s = new Song();
            s.setSongId(rs.getInt("歌曲编号"));
            s.setTitle(rs.getString("歌名"));
            s.setArtist(rs.getString("歌手"));
            return s;
        }
    }

    private static class HotRowMapper implements RowMapper<Song> {
        public Song mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Song s = new Song();
            s.setSongId(rs.getInt("歌曲编号"));
            s.setTitle(rs.getString("歌名"));
            s.setArtist(rs.getString("歌手"));
            try {
                s.setPlayCount(rs.getInt("总点播次数"));
            }catch (Exception e){
                s.setPlayCount(0);
            }

            return s;
        }
    }
}