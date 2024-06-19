package com.bjtu.douyin.controller;

import com.bjtu.douyin.service.OssGetService;
import com.bjtu.douyin.service.OssPushService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@RestController
@RequestMapping("/user/{uid}")
public class OssGetController {

    @GetMapping("/videoss")
    public void getVideo(@RequestParam("videoName") String videoName, HttpServletResponse response) {
        try {
            InputStream videoStream = OssGetService.getFile(videoName);

            if (videoStream != null) {
                response.setContentType("video/mp4");
                StreamUtils.copy(videoStream, response.getOutputStream());
                response.flushBuffer();
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.getWriter().write("视频未找到");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
