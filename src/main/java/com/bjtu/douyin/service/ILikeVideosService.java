package com.bjtu.douyin.service;

import com.bjtu.douyin.entity.LikeVideos;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户喜欢过的视频 服务类
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-27
 */
public interface ILikeVideosService extends IService<LikeVideos> {

    void likeAVideo(Integer uid, Integer id);

    LikeVideos getLikeByIds(Integer uid, Integer id);

    void cancelMyLike(Integer uid, Integer id);
}
