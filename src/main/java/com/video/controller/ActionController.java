package com.video.controller;

import com.video.common.Result;
import com.video.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/action")
public class ActionController {

    @Autowired
    private UserActionService userActionService;

    @PostMapping("/like/{videoId}")
    public Result<Map<String, Object>> likeVideo(@PathVariable Long videoId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean isLiked = userActionService.likeVideo(userId, videoId);
        Map<String, Object> result = new HashMap<>();
        result.put("isLiked", isLiked);
        result.put("message", isLiked ? "点赞成功" : "取消点赞");
        return Result.success(result);
    }

    @PostMapping("/collect/{videoId}")
    public Result<Map<String, Object>> collectVideo(@PathVariable Long videoId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean isCollected = userActionService.collectVideo(userId, videoId);
        Map<String, Object> result = new HashMap<>();
        result.put("isCollected", isCollected);
        result.put("message", isCollected ? "收藏成功" : "取消收藏");
        return Result.success(result);
    }
}