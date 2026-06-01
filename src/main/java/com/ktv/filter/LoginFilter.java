// src/main/java/com/ktv/filter/LoginFilter.java
package com.ktv.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class LoginFilter implements Filter {

    // 不需要登录即可访问的路径
    private List<String> excludePaths = Arrays.asList(
            "/login", "/logout", "/register",
            "/api/auth/login",    // ✅ 添加 API 登录路径
            "/api/auth/register", // ✅ 添加 API 注册路径
            "/login.jsp",         // ✅ 添加登录页面
            "/register.jsp",      // ✅ 添加注册页面
            "/static/", "/css/", "/js/", "/images/",
            "/common/",           // ✅ 添加公共资源
            "/index.html",         // ✅ 首页可能也需要排除
            "/api/auth/current",  // ✅ 获取当前用户信息
            "/error"              // ✅ 错误页面
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 打印调试信息
        System.out.println("Filter 拦截路径: " + path);
        System.out.println("Session: " + session);
        if (session != null) {
            System.out.println("loginUser: " + session.getAttribute("loginUser"));
            System.out.println("user: " + session.getAttribute("user"));
        }

        // 检查是否是排除路径
        boolean isExclude = excludePaths.stream().anyMatch(path::startsWith);

        // 如果用户未登录且访问的是需要登录的页面，重定向到登录页
        if (!isExclude) {
            // 检查两个可能的 Session 属性名
            Object userObj = null;
            if (session != null) {
                userObj = session.getAttribute("loginUser");
                if (userObj == null) {
                    userObj = session.getAttribute("user");
                }
            }

            if (userObj == null) {
                System.out.println("用户未登录，重定向到登录页");
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
        }

        System.out.println("Filter 放行: " + path);
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // 销毁
    }
}