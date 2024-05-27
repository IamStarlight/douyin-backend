package com.bjtu.douyin.controller;


import com.bjtu.douyin.entity.Video;
import com.bjtu.douyin.model.Result;
import com.bjtu.douyin.service.impl.VideoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 视频管理
 */
@RestController
public class VideoController {

    @Autowired
    private VideoServiceImpl videoService;

    /**
     * 上传视频
     * @param uid
     * @param video
     * @return
     */
    @PostMapping("/user/{uid}/videos")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> uploadAVideo(@PathVariable Integer uid,
                                               @RequestBody Video video){
        video.setUploaderId(uid);
        videoService.uploadAVideo(video);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }

    /**
     * 获取某用户发布的视频
     * @param uid
     * @return
     */
    @GetMapping("/user/{uid}/videos")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> getVideoByUploader(@PathVariable Integer uid){
        return new ResponseEntity<>(Result.success(videoService.getVideoByUploader(uid)), HttpStatus.OK);
    }

    /**
     * 获取推荐视频
     * @return
     */
    @GetMapping("/videos")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> getRecommendVideo(){
        return new ResponseEntity<>(Result.success(videoService.getRecommendVideo()), HttpStatus.OK);
    }

    /**
     * 删除某用户发布的视频
     * @param id
     * @return
     */
    @DeleteMapping("/user/{uid}/videos")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> deleteMyVideo(@PathVariable Integer id){
        videoService.deleteMyVideo(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
