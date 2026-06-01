package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.dao.WaiterDAO;
import com.ktv.entity.Waiter;
import com.ktv.service.AuthService;
import com.ktv.service.WaiterService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/waiter")
public class WaiterController {
    @Autowired private WaiterService waiterService;

    @GetMapping("/list")
    public R<List<Waiter>> listAll() {
        return R.ok(waiterService.listAll());
    }

    @GetMapping("/contact")
    public R<Waiter> contact(@RequestParam String waiterId) {
        return R.ok(waiterService.getContact(waiterId));
    }
    @PostMapping("/manage")
    public R<Void> saveWaiter(@RequestBody Waiter w){
        return waiterService.saveWaiter(w) ? R.ok(null) : R.fail("保存失败");
    }
    @DeleteMapping("/manage/{ids}")
    public R<Void> deleteWaiter(@PathVariable List<String> ids){
        waiterService.deleteBatchWaiter(ids);
        return R.ok(null);
    }

}