package com.ktv.service;

import com.ktv.dao.CustomerDAO;
import com.ktv.dao.UserLoginDAO;
import com.ktv.dao.WaiterDAO;
import com.ktv.entity.Customer;
import com.ktv.entity.UserLogin;
import com.ktv.entity.Waiter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Resource
    private UserLoginDAO userLoginDAO;

    @Resource
    private CustomerDAO customerDAO;

    @Resource
    private WaiterDAO waiterDAO;

    // 登录方法 - 添加captcha参数但不验证（前端已验证）
    public Map<String, Object> login(String account, String password, String role, String captcha) {
        Map<String, Object> result = new HashMap<>();

        System.out.println("登录参数：account=" + account + ", role=" + role + ", captcha=" + captcha);
        UserLogin user = userLoginDAO.getUserByAccount(account);
        System.out.println("数据库查询结果：user=" + user);
        if (user != null) {
            System.out.println("数据库角色：" + user.getRole() + "，输入角色：" + role);
            System.out.println("密码匹配：" + user.getPassword().equals(password));
        }

        // 1. 格式校验
        if (!userLoginDAO.isValidAccount(account)) {
            return fail("账号格式 4-20 位字母数字下划线");
        }
        if (!userLoginDAO.isValidPassword(password)) {
            return fail("密码长度 6-50");
        }

        // 2. 验证用户
        if (user == null || !user.getPassword().equals(password) || !user.getRole().equals(role)) {
            return fail("账号或密码或角色错误");
        }

        // 3. 按角色加载详情并设置跳转路径
        String redirectPath = "";
        if ("顾客".equals(role)) {
            Customer c = customerDAO.getCustomerById(account);
            if (c == null) {
                return fail("顾客信息不存在");
            }
            result.put("detail", c);
            redirectPath = "customer";
        } else if ("服务员".equals(role)) {
            Waiter w = waiterDAO.getById(account);
            if (w == null) {
                return fail("服务员信息不存在");
            }
            result.put("detail", w);
            redirectPath = "waiter";
        }

        // 4. 返回结果
        result.put("user", user);
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("redirect", redirectPath);
        result.put("role", role);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> register(String account, String password, String role,
                                        String name, String phone, String gender) {
        // 1 格式 & 重复校验
        if (!userLoginDAO.isValidAccount(account) || !userLoginDAO.isValidPassword(password)) {
            return fail("账号或密码格式不符");
        }
        if (userLoginDAO.exist(account)) return fail("账号已存在");

        // 2 写用户登录表
        UserLogin ul = new UserLogin(account, password, role);
        if (!userLoginDAO.addUser(ul)) return fail("创建用户失败");

        // 3 写顾客表（顾客角色）
        if ("顾客".equals(role)) {
            Customer c = new Customer(account, name, gender, phone);
            if (!customerDAO.addCustomer(c)) {
                throw new RuntimeException("创建顾客失败");
            }
        }
        return success("注册成功");
    }

    public boolean changePassword(String account, String oldPwd, String newPwd) {
        if (!userLoginDAO.isValidPassword(newPwd)) return false;
        UserLogin u = userLoginDAO.getUserByAccount(account);
        System.out.println(u.getPassword());
        if (u == null || !u.getPassword().equals(oldPwd)) return false;
        return userLoginDAO.changePassword(account, newPwd);
    }

    public boolean saveUser(UserLogin u){
        return userLoginDAO.getUserByAccount(u.getAccount()) == null
                ? userLoginDAO.addUser(u)
                : userLoginDAO.updateUser(u);
    }

    public boolean deleteBatchUser(List<String> ids){
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return userLoginDAO.executeUpdate("DELETE FROM 用户登录 WHERE 账号 IN (" + qs + ")", ids.toArray()) > 0;
    }

    public UserLogin getByAccount(String account) {
        return userLoginDAO.getUserByAccount(account);
    }

    private Map<String, Object> success(String msg) {
        Map<String, Object> r = new HashMap<>();
        r.put("success", true);
        r.put("message", msg);
        return r;
    }

    private Map<String, Object> fail(String msg) {
        Map<String, Object> r = new HashMap<>();
        r.put("success", false);
        r.put("message", msg);
        return r;
    }
}