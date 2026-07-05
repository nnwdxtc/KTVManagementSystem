package com.ktv.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);

    private List<String> excludePaths = Arrays.asList(
            "/login", "/logout", "/register",
            "/api/auth/login",
            "/api/auth/register",
            "/login.jsp",
            "/register.jsp",
            "/static/", "/css/", "/js/", "/images/",
            "/common/",
            "/index.html",
            "/api/auth/current",
            "/error"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI().substring(req.getContextPath().length());

        log.debug("Filter 拦截路径: {}", path);
        log.debug("Session 是否存在: {}", session != null);

        boolean isExclude = excludePaths.stream().anyMatch(path::startsWith);

        if (!isExclude) {
            Object userObj = null;
            if (session != null) {
                userObj = session.getAttribute("loginUser");
                if (userObj == null) {
                    userObj = session.getAttribute("user");
                }
            }

            if (userObj == null) {
                log.info("用户未登录，重定向到登录页: {}", path);
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
        }

        log.debug("Filter 放行: {}", path);
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // 销毁
    }
}