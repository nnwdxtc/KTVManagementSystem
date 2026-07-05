package com.ktv.service;

import com.ktv.dao.SongDAO;
import com.ktv.dao.SongOrderDAO;
import com.ktv.entity.Song;
import com.ktv.entity.SongOrder;
import com.ktv.exception.BizException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SongService {

    @Resource
    private SongDAO songDAO;          // 鍘绘帀 new SongDAO()

    @Resource
    private SongOrderDAO orderDAO;    // 璁?Spring 涓�璧锋敞鍏?

    /* 鐐规瓕 */
    public void orderSong(String customerId, int songId) {
        Song s = songDAO.getSongById(songId);
        if (s == null) throw new BizException("Song not found");
        SongOrder o = new SongOrder();
        o.setAccount(customerId);
        o.setSongId(songId);
        if (!orderDAO.insert(o)) throw new BizException("Order song failed");
    }

    /* 鍒犻櫎宸茬偣 */
    public void removeOrderedSong(String customerId, int songId) {
        if (!orderDAO.delete(customerId,songId)) throw new BizException("Remove failed");
    }
    public boolean saveSong(Song s) {
        return s.getSongId() == 0 ? songDAO.addSong(s) :  songDAO.updateSong(s);
    }
    public boolean deleteSong(int id) {
        return  songDAO.deleteSong(id);
    }
    public boolean deleteBatchSong(List<Integer> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return  songDAO.executeUpdate("DELETE FROM 歌曲 WHERE 歌曲编号 IN (" + qs + ")", ids.toArray()) > 0;
    }
    /* 鎼滅储 */
    public List<Song> searchSongs(String keyword) {
        return songDAO.getSongsByName(keyword);
    }

    /* 鐑棬姒?*/
    public List<Song> getHotSongs() {
        return songDAO.getHotSongs();
    }

    /* 椤惧宸茬偣鍒楄〃 */
    public List<SongOrder> getMyOrders(String customerId) {
        return orderDAO.listByCustomer(customerId);
    }

    /* ===== 姝屾洸绠＄悊-鍒嗛〉锛圝DBC 鐗堬級 ===== */
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<Song> pageSong(int current, int size, String keyword){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Song> p =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        return songDAO.page(p, keyword);   // 鐢ㄤ綘鑷繁鍐欑殑 JDBC 鍒嗛〉
    }




}