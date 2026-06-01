package com.ktv.controller;

import com.ktv.common.R;

import com.ktv.entity.UserLogin;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/current")
public class CurrentUserController {

    @GetMapping
    public R<Map<String, String>> current(HttpSession session) {
        UserLogin loginUser = (UserLogin) session.getAttribute("loginUser");
        if (loginUser == null) {
            return R.fail("未登录");
        }
        Map<String, String> map = new HashMap<>();
        map.put("account", loginUser.getAccount());
        map.put("role",    loginUser.getRole());   // CUSTOMER / WAITER
        return R.ok(map);
    }
}