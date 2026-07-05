package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.dao.KTVRoomDAO;
import com.ktv.dto.EndRoomDTO;
import com.ktv.dto.ReserveDTO;
import com.ktv.dto.StartRoomDTO;
import com.ktv.entity.KTVRoom;
import com.ktv.entity.Reservation;
import com.ktv.service.KTVRoomService;
import com.ktv.service.ReservationService;
import com.ktv.service.RoomUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired private KTVRoomService roomService;

    @Autowired private KTVRoomDAO ktvRoomDAO;
    @Autowired private ReservationService reservationService;
    @Autowired private RoomUsageService usageService;

    @GetMapping("/list")
    public R<List<KTVRoom>> listRooms() {
        return R.ok(roomService.listRooms());
    }

    @GetMapping("/rooms")
    public R<List<KTVRoom>> listRoom() {
        return R.ok(roomService.list());
    }

    @PostMapping("/add")
    public R<Void> addRoom(@RequestBody KTVRoom room) {
        if (roomService.getRoomDetail(room.getRoomId()) == null ) {
            roomService.saveRoom(room);
            return R.ok(null);
        }else {
            return R.fail("房间号重复，添加失败");
        }
    }

    @DeleteMapping("/delete")
    public R<Void> deleteRoom(@RequestParam("roomId") String roomId) {
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

    @PostMapping("/reserve")
    public R<Void> reserve(@RequestBody ReserveDTO dto) {
        boolean success = reservationService.createReservation(
                dto.getCustomerId(),
                dto.getRoomNo(),
                dto.getStartTime(),
                dto.getEndTime());
        return success ? R.ok(null) : R.fail("预约失败，该时段已被预约");
    }

    @DeleteMapping("/reserve")
    public R<Void> cancel(
            @RequestParam(value = "customerId", required = false) String customerId,
            @RequestParam(value = "roomNo", required = false) String roomNo,
            @RequestParam(value = "reservationId", required = false) Integer reservationId) {

        if (reservationId != null && reservationId > 0) {
            boolean success = reservationService.deleteReservationById(reservationId);
            return success ? R.ok(null) : R.fail("取消预约失败");
        } else if (customerId != null && !customerId.isEmpty() && roomNo != null && !roomNo.isEmpty()) {
            reservationService.deleteReservation(customerId, roomNo);
            return R.ok(null);
        } else {
            return R.fail("缺少取消预约的必要参数");
        }
    }

    @GetMapping("/reservations")
    public R<List<Reservation>> getMyReservations(@RequestParam("customerId") String customerId) {
        List<Reservation> list = reservationService.listByCustomer(customerId);
        return R.ok(list);
    }

    @GetMapping("/allReservations")
    public R<List<Reservation>> getAllReservations() {
        List<Reservation> list = reservationService.listAll();
        return R.ok(list);
    }

    /* ✅ 修复：开房 - String 转 LocalDateTime */
    @PostMapping("/start")
    public R<Void> start(@RequestBody StartRoomDTO dto) {
        LocalDateTime startTime = LocalDateTime.parse(dto.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(dto.getEndTime());

        usageService.startUsingRoom(
                dto.getWaiterId(),
                dto.getRoomNo(),
                dto.getRoomStatus(),
                startTime,
                endTime);
        return R.ok(null);
    }

    /* ✅ 修复：结账 - String 转 LocalDateTime */
    @PostMapping("/end")
    public R<Void> end(@RequestBody EndRoomDTO dto) {
        LocalDateTime endTime = LocalDateTime.parse(dto.getEndTime());
        usageService.endUsingRoom(dto.getRoomNo(), endTime);
        return R.ok(null);
    }
}