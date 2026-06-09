package com.video.service;

import com.video.entity.Danmaku;
import java.util.List;

public interface DanmakuService {
    List<Danmaku> getDanmakuByVideoId(Long videoId);
    void addDanmaku(Danmaku danmaku);
}