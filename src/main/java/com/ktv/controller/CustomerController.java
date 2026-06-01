package com.ktv.controller;
import com.ktv.common.R;
import com.ktv.dao.CustomerDAO;
import com.ktv.entity.Customer;
import com.ktv.entity.Reservation;
import com.ktv.service.AuthService;
import com.ktv.service.CustomerService;
import com.ktv.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired private CustomerService customerService;

    @GetMapping("/profile")
    public R<Customer> profile(@RequestParam String customerId) {
        return R.ok(customerService.getProfile(customerId));
    }

    @PutMapping("/phone")
    public R<Void> updatePhone(@RequestBody Map<String,String> dto) {
        String customerId = dto.get("customerId");
        String newPhone   = dto.get("newPhone");
        customerService.updatePhone(customerId, newPhone);
        return R.ok(null);
    }
    @PostMapping("/manage")
    public R<Void> saveCustomer(@RequestBody Customer c){
        return customerService.saveCustomer(c) ? R.ok(null) : R.fail("保存失败");
    }
    @DeleteMapping("/manage/{ids}")
    public R<Void> deleteCustomer(@PathVariable List<String> ids){
        customerService.deleteBatchCustomer(ids);
        return R.ok(null);
    }
    @RestController
    @RequestMapping("/api/reservation")
    public class ReservationController {
        @Resource
        private ReservationService reservationService;

        @PostMapping("/manage")
        public R<Void> saveReservation(@RequestBody Reservation r){
            return reservationService.saveReservation(r) ? R.ok(null) : R.fail("保存失败");
        }
        @DeleteMapping("/manage")
        public R<Void> deleteReservation(@RequestParam String account, @RequestParam String roomId){
            reservationService.deleteReservation(account, roomId);
            return R.ok(null);
        }
        /* 如需修改时段，直接复用 saveReservation（主键存在即更新） */
    }
}