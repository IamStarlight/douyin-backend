package com.bjtu.douyin.controller;

import com.bjtu.douyin.model.Result;
import com.bjtu.douyin.utils.OssUtil;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/user/{uid}/file")
public class OssController {

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Result> uploadVideoFile(@RequestParam("file") MultipartFile file) {
        // 将MultipartFile转换为File
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);

        // 上传文件并获取URL
        String objectName = "video/" + file.getOriginalFilename();
        String url = OssUtil.uploadFile(objectName, convFile.getAbsolutePath());

        return new ResponseEntity<>(Result.success(url), HttpStatus.CREATED);

//        try {
//            System.out.println("Received file: " + file.getOriginalFilename());
//
//            // 将MultipartFile转换为File
//            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
//            file.transferTo(convFile);
////            System.out.println("File converted to: " + convFile.getAbsolutePath());
//
//            // 上传文件并获取URL
//            String objectName = "video/" + file.getOriginalFilename();
//            String url = OssServiceImpl.uploadFile(objectName, convFile.getAbsolutePath());
//
//            if (url != null) {
//                System.out.println("Upload successful. File URL: " + url);
//                return new ResponseEntity<>((Result.success()), HttpStatus.CREATED);
////                return ResponseEntity.ok(url);
//            } else {
//                System.out.println("Upload failed.");
//                throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to upload video");
////                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred: " + e.getMessage());
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
//        }
    }

//    @GetMapping
//    public void getVideoFileByName(@RequestParam("videoName") String videoName, HttpServletResponse response) {
//        try {
//            InputStream videoStream = OssServiceImpl.getFile(videoName);
//
//            if (videoStream != null) {
//                response.setContentType("video/mp4");
//                StreamUtils.copy(videoStream, response.getOutputStream());
//                response.flushBuffer();
//            } else {
//                response.setStatus(HttpStatus.NOT_FOUND.value());
//                response.getWriter().write("视频未找到");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//    }
}
