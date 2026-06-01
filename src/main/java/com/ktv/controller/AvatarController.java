package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.entity.UserLogin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.*;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    /** 头像保存目录，指向 classpath:/static/upload/avatar/ */
    private static final String AVATAR_DIR = "static/upload/avatar/";

    /** 获取当前用户账号（沿用你原来的 session 逻辑） */
    private String currAccount(HttpSession session) {
        UserLogin user = (UserLogin) session.getAttribute("user");
        return user == null ? null : user.getAccount();
    }

    /* 1. 上传头像 ---------------------------------------------------- */
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file,
                            HttpSession session) throws IOException {
        String account = currAccount(session);
        if (account == null) return R.fail("未登录");

        // 只接受 jpg/png
        String contentType = file.getContentType();
        if (!MediaType.IMAGE_JPEG_VALUE.equals(contentType)
                && !MediaType.IMAGE_PNG_VALUE.equals(contentType)) {
            return R.fail("仅支持 JPG/PNG");
        }

        // 后缀
        String ext = contentType.endsWith("jpeg") ? ".jpg" : ".png";
        String fileName = account + ext;

        // 确保目录存在
        Path dir = Paths.get("src/main/resources", AVATAR_DIR);
        Files.createDirectories(dir);

        // 保存
        Path target = dir.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return R.ok("上传成功");
    }

    /* 2. 读取头像（含默认头像）---------------------------------------- */
    @GetMapping
    public void avatar(HttpSession session, HttpServletResponse resp) throws IOException {
        String account = currAccount(session);
        if (account == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 先找用户专属头像
        Path dir  = Paths.get("src/main/resources", AVATAR_DIR);
        Path jpg  = dir.resolve(account + ".jpg");
        Path png  = dir.resolve(account + ".png");
        Path target = null;
        if (Files.exists(jpg)) target = jpg;
        else if (Files.exists(png)) target = png;

        // 没有就用默认
        if (target == null) {
            ClassPathResource defaultAvatar = new ClassPathResource(AVATAR_DIR + "default.png");
            resp.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(defaultAvatar.getInputStream(), resp.getOutputStream());
            return;
        }

        // 返回用户头像
        String ext = target.toString().endsWith("jpg") ? MediaType.IMAGE_JPEG_VALUE
                : MediaType.IMAGE_PNG_VALUE;
        resp.setContentType(ext);
        Files.copy(target, resp.getOutputStream());
    }
}