package com.bjtu.douyin.controller;

import com.bjtu.douyin.service.OssPushService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/user/{uid}")
public class OssPushController {

    @PostMapping("/file")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Received file: " + file.getOriginalFilename());

            // 将MultipartFile转换为File
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            System.out.println("File converted to: " + convFile.getAbsolutePath());

            // 上传文件并获取URL
            String objectName = "video/" + file.getOriginalFilename();
            String url = OssPushService.uploadFile(objectName, convFile.getAbsolutePath());

            if (url != null) {
                System.out.println("Upload successful. File URL: " + url);
                return ResponseEntity.ok(url);
            } else {
                System.out.println("Upload failed.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
