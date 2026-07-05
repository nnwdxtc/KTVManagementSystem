package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.dto.ChangePwdDTO;
import com.ktv.dto.LoginDTO;
import com.ktv.dto.RegisterDTO;
import com.ktv.entity.UserLogin;
import com.ktv.service.AuthService;
import com.ktv.dao.UserLoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserLoginDAO userLoginDAO;

    @PostMapping("/login")
    public R<Map<String,Object>> login(@Valid @RequestBody LoginDTO dto,
                                       HttpSession session) {
        // 调用修改后的login方法，传入验证码参数
        Map<String,Object> map = authService.login(
                dto.getAccount(),
                dto.getPassword(),
                dto.getRole(),
                dto.getCaptcha()  // 新增验证码参数
        );

        if ((Boolean) map.get("success")) {
            UserLogin user = userLoginDAO.getUserByAccount(dto.getAccount());
            session.setAttribute("user", user);
        }
        return (Boolean) map.get("success") ? R.ok(map) : R.fail((String) map.get("message"));
    }

    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        Map<String,Object> map = authService.register(
                dto.getAccount(), dto.getPassword(), dto.getRole(),
                dto.getName(), dto.getPhone(), dto.getGender());
        return (Boolean) map.get("success") ? R.ok(null) : R.fail((String) map.get("message"));
    }

    @PostMapping("/changePwd")
    public R<Void> changePwd(@Valid @RequestBody ChangePwdDTO dto) {
        boolean ok = authService.changePassword(dto.getAccount(), dto.getOldPwd(), dto.getNewPwd());
        return ok ? R.ok(null) : R.fail("原密码错误或格式不符");
    }

    @PostMapping("/manage")
    public R<Void> saveUser(@RequestBody UserLogin u){
        return authService.saveUser(u) ? R.ok(null) : R.fail("保存失败");
    }

    @GetMapping("/current")
    public R<UserLogin> current(HttpSession session) {
        UserLogin user = (UserLogin) session.getAttribute("user");
        return user != null ? R.ok(user) : R.fail("未登录");
    }

    @DeleteMapping("/manage/{ids}")
    public R<Void> deleteUser(@PathVariable("ids") List<String> ids){
        authService.deleteBatchUser(ids);
        return R.ok(null);
    }
}