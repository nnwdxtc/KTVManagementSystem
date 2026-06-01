package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.dao.SongDAO;
import com.ktv.dao.WaiterDAO;
import com.ktv.entity.Song;
import com.ktv.entity.SongOrder;
import com.ktv.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/song")
public class SongController {
    @Autowired private SongService songService;

    @GetMapping("/hot")
    public R<List<Song>> hot(@RequestParam(defaultValue = "20") int limit) {
        return R.ok(songService.getHotSongs());
    }

    @GetMapping("/search")
    public R<List<Song>> search(@RequestParam String keyword) {
        return R.ok(songService.searchSongs(keyword));
    }

    /* 点歌 */
    @PostMapping("/order")
    public R<Void> order(@RequestParam String customerId,
                         @RequestParam int songId) {
        songService.orderSong(customerId, songId);
        return R.ok(null);
    }

    /* 删除已点 */
    @DeleteMapping("/order")
    public R<Void> remove(@RequestParam String customerId,@RequestParam int songId) {
        songService.removeOrderedSong(customerId,songId);
        return R.ok(null);
    }

    /* 我的已点 */
    @GetMapping("/my")
    public R<List<SongOrder>> myOrders(@RequestParam String customerId) {
        return R.ok(songService.getMyOrders(customerId));
    }
    /* 追加到 SongController.java 最下面 */
    /* --------- 歌曲管理（复用 /api/song 前缀） --------- */
    @GetMapping("/manage/page")
    public R<Page<Song>> pageSong(@RequestParam(defaultValue = "1") int current,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String keyword){
        return R.ok(songService.pageSong(current, size, keyword));
    }

    @DeleteMapping("/manage")
    public R<Void> delSongs(@RequestParam List<Integer> ids){
        try {
            songService.removeSongs(ids);
            return R.ok(null);
        }catch (Exception e){
            return R.fail("删除失败,正在被使用");
        }
    }

    @PostMapping("/manage")
    public R<Void> saveSong(@RequestBody Song s){
        return songService.saveSong(s) ? R.ok(null) : R.fail("保存失败");
    }

    @DeleteMapping("/manage/{ids}")
    public R<Void> deleteSong(@PathVariable List<Integer> ids){
        try {
            songService.removeSongs(ids);
            return R.ok(null);
        }catch (Exception e){
            return R.fail("删除失败,正在被使用");
        }

    }

}