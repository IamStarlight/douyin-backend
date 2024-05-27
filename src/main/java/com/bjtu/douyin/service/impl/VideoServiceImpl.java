package com.bjtu.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjtu.douyin.entity.Video;
import com.bjtu.douyin.mapper.VideoMapper;
import com.bjtu.douyin.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频表 服务实现类
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-27
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

    @Autowired
    private VideoMapper videoMapper;

    public void uploadAVideo(Video video) {
        video.setDeleted(false);
        video.setLikeCount(0);
        video.setReleaseDate(LocalDateTime.now());
    }

    public Page<Map<String,Object>> getVideoByUploader(Integer currentPage, Integer pageSize, Integer uid) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getUploaderId,uid)
                .select(Video::getId,Video::getTitle,Video::getReleaseDate,Video::getLikeCount,Video::getUrl);
        return videoMapper.selectMapsPage(new Page<>(currentPage,pageSize),wrapper);
    }

    public List<Map<String,Object>> getRecommendVideo() {
        //todo: 访问过的不推荐
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Video::getId,Video::getTitle,Video::getReleaseDate,Video::getLikeCount,Video::getUrl)
                .orderByDesc(Video::getLikeCount);
        return listMaps(wrapper);
    }

    public void deleteMyVideo(Integer id) {
        LambdaUpdateWrapper<Video> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Video::getId,id)
                .set(Video::getDeleted,true);

        update(wrapper);
    }
}
