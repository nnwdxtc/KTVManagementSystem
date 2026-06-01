package com.ktv.service;

import com.ktv.dao.SongDAO;
import com.ktv.dao.SongOrderDAO;
import com.ktv.entity.Song;
import com.ktv.entity.SongOrder;
import com.ktv.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class SongOrderService {

    @Resource

    private SongOrderDAO songOrderDAO;
    @Resource
    private SongDAO songDAO;

    public void addSongOrder(String customerId, int songId) {
        Song song = songDAO.getSongById(songId);
        if (song == null) throw new BizException("Song not found");
        SongOrder o = new SongOrder();
        o.setAccount(customerId);
        o.setSongId(songId);
        o.setPlayCount(1);
        if (!songOrderDAO.insert(o)) throw new BizException("Add song order failed");
        // 同时点播次数+1
        songDAO.increasePlayCount(customerId,songId);
    }



    public List<SongOrder> listByCustomer(String customerId) {
        return songOrderDAO.listByCustomer(customerId);
    }

    public List<Song> searchSongs(String keyword) {
        return songDAO.getSongsByName(keyword);
    }

    public List<Song> getHotSongs() {
        return songDAO.getHotSongs();
    }
}