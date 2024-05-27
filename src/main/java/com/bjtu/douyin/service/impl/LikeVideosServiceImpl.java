package com.bjtu.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bjtu.douyin.entity.LikeVideos;
import com.bjtu.douyin.exception.ServiceException;
import com.bjtu.douyin.mapper.LikeVideosMapper;
import com.bjtu.douyin.service.ILikeVideosService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jdk.jfr.Timestamp;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用户喜欢过的视频 服务实现类
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-27
 */
@Service
public class LikeVideosServiceImpl extends ServiceImpl<LikeVideosMapper, LikeVideos> implements ILikeVideosService {

    public void likeAVideo(Integer uid, Integer id) {
        if(!Objects.isNull(getLikeByIds(uid,id))){
            throw new ServiceException(HttpStatus.FORBIDDEN.value(), "已点过赞");
        }
        LikeVideos likeVideos = new LikeVideos();
        likeVideos.setUserId(uid);
        likeVideos.setVideoId(id);
        likeVideos.setTimestamp(LocalDateTime.now());
        save(likeVideos);
    }

    private LikeVideos getLikeByIds(Integer uid, Integer id) {
        LambdaQueryWrapper<LikeVideos> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeVideos::getUserId,uid)
                .eq(LikeVideos::getVideoId,id);
        return getOne(wrapper);
    }

    public void cancelMyLike(Integer uid, Integer id) {
        if(Objects.isNull(getLikeByIds(uid,id))){
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "已取消点赞");
        }
        LambdaUpdateWrapper<LikeVideos> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LikeVideos::getUserId,uid)
                .eq(LikeVideos::getVideoId,id);
        remove(wrapper);
    }


}
