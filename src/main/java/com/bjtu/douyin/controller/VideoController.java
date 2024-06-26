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
    //@PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> uploadAVideo(@PathVariable Integer uid,
                                               @RequestBody Video video){
        video.setUploaderId(uid);
        videoService.uploadAVideo(video);
        return new ResponseEntity<>(Result.success(), HttpStatus.CREATED);
    }

    /**
     * 修改视频
     * @param uid
     * @param video
     * @return
     */
    @PutMapping("/user/{uid}/videos")
    //@PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> modifyVideoInfo(@PathVariable Integer uid,
                                                    @RequestBody Video video){
        video.setUploaderId(uid);
        videoService.modifyVideoInfo(video);
        return new ResponseEntity<>(Result.success(), HttpStatus.CREATED);
    }

    /**
     * 标记浏览视频
     * @param uid
     * @param id
     * @return
     */
    @PutMapping("/user/{uid}/videos/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> userWatchVideo(@PathVariable Integer uid,
                                               @PathVariable Integer id){
        videoService.userWatchVideo(uid,id);
        return new ResponseEntity<>(Result.success(), HttpStatus.CREATED);
    }

    /**
     * 获取某用户发布的视频
     * @param uid
     * @return
     */
    @GetMapping("/user/{uid}/videos")
    //@PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> getVideoByUploader(@PathVariable Integer uid,
                                                     @RequestParam(defaultValue = "1") Integer currentPage,
                                                     @RequestParam(defaultValue = "10") Integer pageSize){
        return new ResponseEntity<>(Result.success(videoService.getVideoByUploader(currentPage,pageSize,uid)), HttpStatus.OK);
    }

    /**
     * 获取推荐视频
     * @return
     */
    @GetMapping("/user/{uid}/videos/recommend")
    //@PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> getRecommendVideo(@PathVariable Integer uid){
        return new ResponseEntity<>(Result.success(videoService.getRecommendVideo(uid)), HttpStatus.OK);
    }

    /**
     * 删除视频
     * @param id
     * @return
     */
    @DeleteMapping("/videos/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> deleteMyVideo(@PathVariable Integer id){
        videoService.deleteMyVideo(id);
        //用户删除数据成功后，返回204
        return new ResponseEntity<>(Result.success(), HttpStatus.NO_CONTENT);
    }
}
