package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.dao.KTVRoomDAO;
import com.ktv.dto.EndRoomDTO;
import com.ktv.dto.ReserveDTO;
import com.ktv.dto.StartRoomDTO;
import com.ktv.entity.KTVRoom;
import com.ktv.entity.Reservation;
import com.ktv.entity.RoomUsage;

import com.ktv.service.KTVRoomService;
import com.ktv.service.ReservationService;
import com.ktv.service.RoomUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired private KTVRoomService roomService;

    @Autowired private KTVRoomDAO ktvRoomDAO;
    @Autowired private ReservationService reservationService;
    @Autowired private RoomUsageService usageService;

    /* 前端：大厅房态大屏 */
    @GetMapping("/list")
    public R<List<KTVRoom>> listRooms() {
        return R.ok(roomService.listRooms());
    }


    @GetMapping("/rooms")
    public R<List<KTVRoom>> listRoom() {
        return R.ok(roomService.list());
    }


    /* 服务员：新增房间 */
    @PostMapping("/add")
    public R<Void> addRoom(@RequestBody KTVRoom room) {
        if (roomService.getRoomDetail(room.getRoomId()) == null ) {
            roomService.saveRoom(room);
            return R.ok(null);
        }else {
            return R.fail("房间号重复，添加失败");
        }
    }

    /* 服务员：删除房间 */
    @DeleteMapping("/delete")
    public R<Void> deleteRoom(@RequestParam String roomId) {
        boolean success=false;
        try {
            success = roomService.deleteRoom(roomId);
            return success ? R.ok(null) : R.fail("删除失败");
        }catch (Exception e){
            return  R.fail("删除失败,房间正在使用");
        }

    }


    @GetMapping("/byStatus")
    public R<List<KTVRoom>> byStatus() {
        return R.ok(roomService.listByStatus());
    }

    /* 顾客：预约房间 */
    @PostMapping("/reserve")
    public R<Void> reserve(@RequestBody ReserveDTO dto) {
        reservationService.createReservation(dto.getCustomerId(),
                dto.getRoomNo(),
                dto.getStartTime(),
                dto.getEndTime());
        return R.ok(null);
    }

    /* 顾客：取消预约 */
    @DeleteMapping("/reserve")
    public R<Void> cancel(@RequestParam String customerId,
                          @RequestParam String roomNo) {
        reservationService.deleteReservation(customerId, roomNo);
        return R.ok(null);
    }

    /* 顾客：查询我的预约记录（调用存储过程） */
    @GetMapping("/reservations")
    public R<List<Reservation>> getMyReservations(@RequestParam String customerId) {
        List<Reservation> list = reservationService.listByCustomer(customerId);
        return R.ok(list);
    }

    /* 服务员：开房 */
    @PostMapping("/start")
    public R<Void> start(@RequestBody StartRoomDTO dto) {
        usageService.startUsingRoom(dto.getWaiterId(),
                dto.getRoomNo(),
                dto.getRoomStatus(),
                dto.getStartTime(),
                dto.getEndTime());
        return R.ok(null);
    }

    /* 服务员：结账 */
    @PostMapping("/end")
    public R<Void> end(@RequestBody EndRoomDTO dto) {
        usageService.endUsingRoom(dto.getRoomNo(), dto.getEndTime());
        return R.ok(null);
    }
    @RestController
    @RequestMapping("/api/room-usage")
    public class RoomUsageController {
        @Resource
        private RoomUsageService roomUsageService;

        @PostMapping("/manage")
        public R<Void> saveUsage(@RequestBody RoomUsage u){
            return roomUsageService.saveUsage(u) ? R.ok(null) : R.fail("保存失败");
        }
        @DeleteMapping("/manage")
        public R<Void> deleteUsage(@RequestParam String account, @RequestParam String roomId){
            roomUsageService.deleteUsage(account, roomId);
            return R.ok(null);
        }

    }

}