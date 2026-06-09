package com.video.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.entity.Video;
import java.util.List;

public interface VideoService {
    IPage<Video> getVideoList(Integer page, Integer size, Long categoryId, String keyword);
    Video getVideoDetail(Long id);
    List<Video> getHotVideos(Integer limit);
    List<Video> getVideosByCategory(Long categoryId, Integer limit);
    void uploadVideo(Video video);
}