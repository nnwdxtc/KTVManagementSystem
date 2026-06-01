package com.ktv.service;

import com.ktv.dao.CustomerDAO;
import com.ktv.dao.KTVRoomDAO;
import com.ktv.dao.ReservationDAO;
import com.ktv.entity.Customer;
import com.ktv.entity.KTVRoom;
import com.ktv.entity.Reservation;
import com.ktv.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Resource
    private ReservationDAO reservationDAO;

    @Resource
    private KTVRoomDAO roomDAO;

    @Resource
    private CustomerDAO customerDAO;

    public void createReservation(String customerId, String roomNo,
                                  LocalDateTime start, LocalDateTime end) {

        /* 1. 基本时间轴合理性（只跟自己比） */
        if (end.isBefore(start)) {
            throw new BizException("结束时间必须晚于开始时间");
        }

        /* 2. 同一顾客、同一房间，自己不能跟自己冲突 */
        List<Reservation> myList = reservationDAO.selectByCustomerAndRoom(customerId, roomNo);
        for (Reservation old : myList) {
            // 新旧时段是否重叠
            boolean overlap = !(end.isEqual(old.getStartTime()) || end.isBefore(old.getStartTime())
                    || start.isEqual(old.getEndTime()) || start.isAfter(old.getEndTime()));
            if (overlap) {
                throw new BizException("您在该房间已存在重叠的预约时段");
            }
        }

        /* 4. 跟别人冲突检测（全局视角） */
        if (reservationDAO.hasConflict(roomNo, start, end)) {
            throw new BizException("该时段已被其他顾客预约");
        }

        /* 5. 顾客存在 */
        if (customerDAO.getCustomerById(customerId) == null) {
            throw new BizException("顾客不存在");
        }

        /* 6. 落库 */
        Reservation r = new Reservation();
        r.setAccount(customerId);
        r.setRoomId(roomNo);
        r.setStartTime(start);
        r.setEndTime(end);
        if (!reservationDAO.insert(r)) {
            throw new BizException("预约失败");
        }
    }
    public boolean saveReservation(Reservation r) {
        return reservationDAO.update(r);   // 只允许改时段
    }
    public boolean deleteReservation(String account, String roomId) {
        return reservationDAO.delete(account, roomId);
    }
    public boolean deleteBatchReservation(List<String> accounts, List<String> roomIds) {
        return reservationDAO.deleteBatch(accounts, roomIds);
    }
    public List<Reservation> listByRoom(String roomNo) {
        return reservationDAO.selectByRoom(roomNo);
    }

    /**
     * 通过存储过程查询顾客预约记录
     */
    public List<Reservation> listByCustomer(String customerId) {
        return reservationDAO.selectByCustomerStoredProcedure(customerId);
    }


}