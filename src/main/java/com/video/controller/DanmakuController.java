package com.video.controller;

import com.video.common.Result;
import com.video.entity.Danmaku;
import com.video.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/danmaku")
public class DanmakuController {

    @Autowired
    private DanmakuService danmakuService;

    @GetMapping("/list/{videoId}")
    public Result<List<Danmaku>> getDanmakuList(@PathVariable Long videoId) {
        return Result.success(danmakuService.getDanmakuByVideoId(videoId));
    }

    @PostMapping("/add")
    public Result<Void> addDanmaku(@RequestBody Danmaku danmaku, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        danmaku.setUserId(userId);
        danmakuService.addDanmaku(danmaku);
        return Result.success("发送成功", null);
    }
}