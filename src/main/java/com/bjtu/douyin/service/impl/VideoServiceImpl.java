package com.bjtu.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjtu.douyin.entity.Video;
import com.bjtu.douyin.exception.ServiceException;
import com.bjtu.douyin.mapper.VideoMapper;
import com.bjtu.douyin.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    // 创建布隆过滤器，设置存储的数据类型，预期数据量，误判率 (必须大于0，小于1)
    private final int insertions = 10000000;
    private final double fpp = 0.0001;

    // 创建一个映射，其中键是用户ID，值是对应的布隆过滤器
    private ConcurrentHashMap<Integer, BloomFilter<String>> bloomFilters = new ConcurrentHashMap<>();

    // 创建一个映射，其中键是用户ID，值是对应的推荐视频队列
    private ConcurrentHashMap<Integer, Queue<Map<String,Object>>> recommendVideoQueues = new ConcurrentHashMap<>();

    @Override
    public void uploadAVideo(Video video) {
        video.setDeleted(false);
        video.setLikeCount(0);
        video.setReleaseDate(LocalDateTime.now());
        save(video);
    }

    @Override
    public Page<Map<String,Object>> getVideoByUploader(Integer currentPage, Integer pageSize, Integer uid) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getUploaderId,uid)
                .eq(Video::getDeleted,false)
                .select(Video::getId,Video::getTitle,Video::getReleaseDate,Video::getLikeCount,Video::getUrl);
        return videoMapper.selectMapsPage(new Page<>(currentPage,pageSize),wrapper);
    }

    @Override
    public List<Map<String,Object>> getRecommendVideo(Integer uid) {
        // 获取用户的布隆过滤器，如果不存在，就创建一个新的
        BloomFilter<String> bloomFilter = bloomFilters.computeIfAbsent(uid, k -> BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), insertions, fpp));

        // 获取用户的推荐视频队列，如果不存在，就创建一个新的
        Queue<Map<String,Object>> recommendVideoQueue = recommendVideoQueues.computeIfAbsent(uid, k -> new LinkedList<>());

        // 如果队列为空，那么获取所有的视频，然后添加到队列中
        if (recommendVideoQueue.isEmpty()) {
            LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(Video::getId,Video::getTitle,Video::getReleaseDate,Video::getLikeCount,Video::getUrl)
                    .orderByDesc(Video::getLikeCount);
            List<Map<String,Object>> allVideos = listMaps(wrapper);

            for (Map<String,Object> video : allVideos) {
                Integer id = (Integer) video.get("id");
                if (!bloomFilter.mightContain(String.valueOf(id))) {
                    recommendVideoQueue.offer(video);
                }
            }
        }

        // 创建一个新的列表来存储推荐的视频
        List<Map<String,Object>> recommendVideos = new ArrayList<>();

        // 从队列中取出前10个视频
        for (int i = 0; i < 10 && !recommendVideoQueue.isEmpty(); i++) {
            recommendVideos.add(recommendVideoQueue.poll());
        }

        return recommendVideos;
    }

    @Override
    public void deleteMyVideo(Integer id) {
        LambdaUpdateWrapper<Video> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Video::getId,id)
                .set(Video::getDeleted,true);

        update(wrapper);
    }

    @Override
    public void userWatchVideo(Integer uid, Integer id) {
        // 获取用户的布隆过滤器，如果不存在，就创建一个新的
        BloomFilter<String> bloomFilter = bloomFilters.computeIfAbsent(uid, k -> BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), insertions, fpp));
        // 将视频ID添加到用户的布隆过滤器中
        bloomFilter.put(String.valueOf(id));
    }

    @Override
    public void modifyVideoInfo(Video video) {
        if(getVideoByIdAndUid(video.getId(),video.getUploaderId()) == null){
            throw new ServiceException(HttpStatus.FORBIDDEN.value(), "视频不存在");
        }
        LambdaUpdateWrapper<Video> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Video::getId,video.getId());
        update(video,wrapper);
    }

    @Override
    public Video getVideoByIdAndUid(Integer id, Integer uploaderId) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getId,id)
                .eq(Video::getUploaderId,uploaderId)
                .eq(Video::getDeleted,false);
        return getOne(wrapper);
    }

    @Override
    public Video getVideoById(Integer id) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getId,id)
                .eq(Video::getDeleted,false);
        return getOne(wrapper);
    }

    @Override
    public void addLikeCount(Integer id) {
        Video video = getById(id);
        video.setLikeCount(video.getLikeCount() + 1);
        updateById(video);
    }

    @Override
    public void reduceLikeCount(Integer id) {
        Video video = getById(id);
        video.setLikeCount(video.getLikeCount() - 1);
        updateById(video);
    }
}
