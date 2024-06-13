package com.bjtu.douyin.controller;


import com.bjtu.douyin.entity.Video;
import com.bjtu.douyin.model.Result;
import com.bjtu.douyin.service.impl.LikeVideosServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 点赞管理
 */
@RestController
public class LikeVideosController {

    @Autowired
    private LikeVideosServiceImpl likeVideosService;

    /**
     * 用户点赞视频
     * @param uid
     * @param id
     * @return
     */
    @PostMapping("/user/{uid}/like/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> likeAVideo(@PathVariable Integer uid,
                                               @PathVariable Integer id){
        likeVideosService.likeAVideo(uid,id);
        return new ResponseEntity<>(Result.success(), HttpStatus.CREATED);
    }

    /**
     * 用户取消点赞视频
     * @param uid
     * @param id
     * @return
     */
    @DeleteMapping("/user/{uid}/like/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> cancelMyLike(@PathVariable Integer uid,
                                                @PathVariable Integer id){
        likeVideosService.cancelMyLike(uid,id);
        return new ResponseEntity<>(Result.success(), HttpStatus.NO_CONTENT);
    }
}
