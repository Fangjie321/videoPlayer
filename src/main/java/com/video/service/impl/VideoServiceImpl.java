package com.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.entity.Video;
import com.video.mapper.UserMapper;
import com.video.mapper.CategoryMapper;
import com.video.mapper.VideoMapper;
import com.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public IPage<Video> getVideoList(Integer page, Integer size, Long categoryId, String keyword) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getStatus, 1);
        if (categoryId != null) wrapper.eq(Video::getCategoryId, categoryId);
        if (StringUtils.hasText(keyword)) wrapper.like(Video::getTitle, keyword);
        wrapper.orderByDesc(Video::getCreateTime);

        Page<Video> videoPage = new Page<>(page, size);
        IPage<Video> result = videoMapper.selectPage(videoPage, wrapper);

        result.getRecords().forEach(video -> {
            video.setUser(userMapper.selectById(video.getUserId()));
            if (video.getCategoryId() != null) video.setCategory(categoryMapper.selectById(video.getCategoryId()));
        });

        return result;
    }

    @Override
    public Video getVideoDetail(Long id) {
        Video video = videoMapper.selectById(id);
        if (video != null) {
            videoMapper.incrementViewCount(id);
            video.setViewCount(video.getViewCount() + 1);
            video.setUser(userMapper.selectById(video.getUserId()));
            if (video.getCategoryId() != null) video.setCategory(categoryMapper.selectById(video.getCategoryId()));
        }
        return video;
    }

    @Override
    public List<Video> getHotVideos(Integer limit) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getStatus, 1).eq(Video::getIsHot, 1)
                .orderByDesc(Video::getViewCount).last("LIMIT " + limit);
        return videoMapper.selectList(wrapper).stream().peek(video -> {
            video.setUser(userMapper.selectById(video.getUserId()));
        }).collect(Collectors.toList());
    }

    @Override
    public List<Video> getVideosByCategory(Long categoryId, Integer limit) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getStatus, 1).eq(Video::getCategoryId, categoryId)
                .orderByDesc(Video::getCreateTime).last("LIMIT " + limit);
        return videoMapper.selectList(wrapper);
    }

    @Override
    public void uploadVideo(Video video) {
        video.setViewCount(0L);
        video.setLikeCount(0L);
        video.setCollectCount(0L);
        video.setCommentCount(0L);
        video.setDanmakuCount(0L);
        video.setStatus(1);
        videoMapper.insert(video);
    }
}