package com.ktv.service;

import com.ktv.dao.KTVRoomDAO;
import com.ktv.dao.ReservationDAO;
import com.ktv.dao.RoomUsageDAO;
import com.ktv.common.Constants;
import com.ktv.entity.KTVRoom;
import com.ktv.entity.Reservation;
import com.ktv.entity.RoomUsage;
import com.ktv.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomUsageService {

    @Resource
    private RoomUsageDAO usageDAO;

    @Resource
    private ReservationDAO reservationDAO;

    @Resource
    private KTVRoomDAO roomDAO;

    public void startUsingRoom(String waiterId, String roomNo,String roomStatus,
                               LocalDateTime start, LocalDateTime end) {



        KTVRoom room = roomDAO.getRoomByNo(roomNo);
        if (room == null || Constants.ROOM_STATUS_UNRESERVED.equals(room.getRoomStatus())) {
            throw new BizException("房间未处于已预约状态");
        }
        if (Constants.ROOM_STATUS_UNRESERVED.equals(roomStatus)) {
            if (Constants.ROOM_STATUS_IN_USE.equals(room.getRoomStatus())) {
                List<Reservation> reservations = reservationDAO.selectByRoom(roomNo);
                String newStatus = (reservations != null && !reservations.isEmpty()) ? Constants.ROOM_STATUS_RESERVED : Constants.ROOM_STATUS_UNRESERVED;
                
                // 删除房间使用记录
                RoomUsage activeUsage = usageDAO.getActive(roomNo);
                if (activeUsage != null) {
                    if (!usageDAO.deleteRoomUsage(activeUsage.getAccount(), roomNo)) {
                        throw new BizException("删除使用记录失败");
                    }
                }
                
                // 修改房间状态
                room.setRoomStatus(newStatus);
                if (!roomDAO.updateRoom(room)) {
                    throw new BizException("Update room status failed");
                }
            } else if (Constants.ROOM_STATUS_RESERVED.equals(room.getRoomStatus())) {

                Reservation r = reservationDAO.select(roomNo);
                if (r == null) {
                    throw new BizException("该房间没有有效预约");
                }
                room.setRoomStatus(Constants.ROOM_STATUS_UNRESERVED);
                if (!roomDAO.updateRoom(room)) {
                    throw new BizException("Update room status failed");
                }
                // 删除预约记录
                if (!reservationDAO.delete(r.getAccount(), roomNo)) {
                    throw new BizException("Delete reservation failed");
                }
            } else {
                throw new BizException("当前房间状态不支持此操作");
            }
        }else {
            Reservation r = reservationDAO.select(roomNo);
            // 修改为使用中：检查时间合法性
            if (start.isAfter(end) || start.isEqual(end)) {
                throw new BizException("开始时间必须早于结束时间");
            }

            // 查询该房间的所有预约记录
            List<Reservation> reservations = reservationDAO.selectByRoom(roomNo);
            if (reservations == null || reservations.isEmpty()) {
                throw new BizException("该房间没有预约记录");
            }

            // 检查使用时间段是否是任意预约时段的子集
            Reservation matchedReservation = null;
            for (Reservation reservation : reservations) {
                if ((start.isEqual(reservation.getStartTime()) || start.isAfter(reservation.getStartTime())) &&
                        (end.isEqual(reservation.getEndTime()) || end.isBefore(reservation.getEndTime()))) {
                    matchedReservation = reservation;
                    break;
                }
            }

            if (matchedReservation == null) {
                throw new BizException("使用时间段必须在某个预约时段范围内");
            }

            // 超时校验：实际开始时间不得落后于预约开始时间超过30分钟
            if (start.isAfter(matchedReservation.getStartTime().plusMinutes(30))) {
                throw new BizException("超时无法使用，请重新输入或改成未预约状态");
            }



            // 写使用记录
            if (!usageDAO.insert(matchedReservation.getAccount(), roomNo, start, end)) {
                throw new BizException("插入使用记录失败");
            }

            // 删除当前预约记录
            if (!reservationDAO.delete(r.getAccount(), roomNo)) {
                throw new BizException("删除预约记录失败");
            }

            room.setSales(room.getSales() + 1);
            room.setRoomStatus(Constants.ROOM_STATUS_IN_USE);
            if (!roomDAO.updateRoom(room)) {
                throw new BizException("更新房间状态失败");
            }
        }

    }

    public void endUsingRoom(String roomNo, LocalDateTime endTime) {
        RoomUsage usage = usageDAO.selectActive(roomNo);
        if (usage == null) {
            throw new BizException("房间未在使用中");
        }
        if (!usageDAO.endUsage(roomNo, endTime)) {
            throw new BizException("结束使用记录失败");
        }
        KTVRoom room = roomDAO.getRoomByNo(roomNo);
        room.setRoomStatus(Constants.ROOM_STATUS_UNRESERVED);
        room.setSales(room.getSales() + 1);
        if (!roomDAO.updateRoom(room)) {
            throw new BizException("更新房间状态失败");
        }
    }

    public void endUsingRoom(String roomNo) {
        endUsingRoom(roomNo, LocalDateTime.now());
    }

    public boolean saveUsage(RoomUsage u) {
        return usageDAO.updateUsage(u);
    }

    public boolean deleteUsage(String account, String roomId) {
        return usageDAO.deleteUsage(account, roomId);
    }

    public boolean deleteBatchUsage(List<String> accounts, List<String> roomIds) {
        return usageDAO.deleteBatch(accounts, roomIds);
    }

    /* 查询 */
    public RoomUsage getActiveUsage(String roomNo) {
        return usageDAO.selectActive(roomNo);
    }
}