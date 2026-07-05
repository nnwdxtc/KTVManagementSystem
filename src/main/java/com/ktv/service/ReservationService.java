package com.ktv.service;

import com.ktv.common.Constants;
import com.ktv.dao.KTVRoomDAO;
import com.ktv.dao.ReservationDAO;
import com.ktv.entity.KTVRoom;
import com.ktv.entity.Reservation;
import com.ktv.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;

    @Autowired
    private KTVRoomDAO roomDAO;

    /**
     * 创建预约（顾客操作）
     * 规则：只能将房间从"未预约"状态变为"已预约"状态
     */
    public boolean createReservation(String customerId, String roomNo, String startTime, String endTime) {
        try {
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);

            // 检查房间状态是否为"未预约"
            KTVRoom room = roomDAO.getRoomByNo(roomNo);
            if (room == null) {
                throw new BizException("房间不存在");
            }
            if (!Constants.ROOM_STATUS_UNRESERVED.equals(room.getRoomStatus())) {
                throw new BizException("房间当前状态不支持预约");
            }

            // 创建预约记录
            Reservation reservation = new Reservation();
            reservation.setAccount(customerId);
            reservation.setRoomId(roomNo);
            reservation.setStartTime(start);
            reservation.setEndTime(end);

            // 检查时间段是否已被预约
            if (reservationDAO.isRoomBooked(roomNo, start, end)) {
                throw new BizException("该时段已被预约");
            }

            // 插入预约记录
            boolean success = reservationDAO.insert(reservation);
            if (!success) {
                throw new BizException("预约失败");
            }

            // 更新房间状态为"已预约"
            room.setRoomStatus(Constants.ROOM_STATUS_RESERVED);
            roomDAO.updateRoom(room);

            return true;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ 保存预约（直接使用 Reservation 对象）
     */
    public boolean saveReservation(Reservation reservation) {
        try {
            return reservationDAO.insert(reservation);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过顾客ID和房间号删除预约
     */
    public void deleteReservation(String customerId, String roomNo) {
        reservationDAO.deleteByCustomerAndRoom(customerId, roomNo);
    }

    /**
     * 通过预约单ID删除预约
     */
    public boolean deleteReservationById(int reservationId) {
        try {
            return reservationDAO.deleteById(reservationId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询顾客的预约列表
     */
    public List<Reservation> listByCustomer(String customerId) {
        return reservationDAO.listByCustomer(customerId);
    }

    /**
     * 获取所有预约记录（服务员用）
     */
    public List<Reservation> listAll() {
        return reservationDAO.getAllReservations();
    }

    /**
     * 通过房间号获取预约记录
     */
    public List<Reservation> listByRoomId(String roomId) {
        return reservationDAO.getByRoomId(roomId);
    }
}