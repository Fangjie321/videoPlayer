package com.video.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.common.Result;
import com.video.entity.Video;
import com.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private com.video.service.UserActionService userActionService;

    @GetMapping("/list")
    public Result<Map<String, Object>> getVideoList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        IPage<Video> videoPage = videoService.getVideoList(page, size, categoryId, keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("records", videoPage.getRecords());
        result.put("total", videoPage.getTotal());
        result.put("current", videoPage.getCurrent());
        result.put("size", videoPage.getSize());
        return Result.success(result);
    }

    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getVideoDetail(@PathVariable Long id, HttpServletRequest request) {
        Video video = videoService.getVideoDetail(id);
        if (video == null) return Result.error("视频不存在");
        Map<String, Object> result = new HashMap<>();
        result.put("video", video);
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            result.put("isLiked", userActionService.isLiked(userId, id));
            result.put("isCollected", userActionService.isCollected(userId, id));
        } else {
            result.put("isLiked", false);
            result.put("isCollected", false);
        }
        return Result.success(result);
    }

    @GetMapping("/hot")
    public Result<List<Video>> getHotVideos(@RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(videoService.getHotVideos(limit));
    }

    @GetMapping("/category/{categoryId}")
    public Result<List<Video>> getVideosByCategory(@PathVariable Long categoryId, @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(videoService.getVideosByCategory(categoryId, limit));
    }

    @PostMapping("/upload")
    public Result<Void> uploadVideo(@RequestBody Video video, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        video.setUserId(userId);
        videoService.uploadVideo(video);
        return Result.success("上传成功", null);
    }
}