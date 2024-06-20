package com.bjtu.douyin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjtu.douyin.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频表 服务类
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-27
 */
public interface IVideoService extends IService<Video> {

    void uploadAVideo(Video video);

    Page<Map<String,Object>> getVideoByUploader(Integer currentPage, Integer pageSize, Integer uid);


    List<Map<String,Object>> getRecommendVideo(Integer uid);

    void deleteMyVideo(Integer id);

    void userWatchVideo(Integer uid, Integer id);

    void modifyVideoInfo(Video video);

    Video getVideoByIdAndUid(Integer id, Integer uploaderId);

    Video getVideoById(Integer id);
}
