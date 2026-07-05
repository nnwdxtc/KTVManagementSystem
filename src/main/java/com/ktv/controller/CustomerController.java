package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.entity.Customer;
import com.ktv.entity.Reservation;
import com.ktv.service.CustomerService;
import com.ktv.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    /**
     * 获取顾客信息
     */
    @GetMapping("/profile")
    public R<Customer> getProfile(@RequestParam("customerId") String customerId) {
        Customer customer = customerService.getByAccount(customerId);
        return customer != null ? R.ok(customer) : R.fail("用户不存在");
    }

    /**
     * 修改手机号
     */
    @PutMapping("/phone")
    public R<Void> updatePhone(@RequestBody Map<String, String> request) {
        try {
            String customerId = request.get("customerId");
            String newPhone = request.get("newPhone");
            boolean success = customerService.updatePhone(customerId, newPhone);
            return success ? R.ok(null) : R.fail("修改失败");
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 获取顾客的预约记录
     */
    @GetMapping("/reservations")
    public R<List<Reservation>> getReservations(@RequestParam("customerId") String customerId) {
        List<Reservation> list = reservationService.listByCustomer(customerId);
        return R.ok(list);
    }

    /**
     * 取消预约
     */
    @DeleteMapping("/reservation")
    public R<Void> cancelReservation(
            @RequestParam("customerId") String customerId,
            @RequestParam("roomNo") String roomNo) {
        reservationService.deleteReservation(customerId, roomNo);
        return R.ok(null);
    }
}