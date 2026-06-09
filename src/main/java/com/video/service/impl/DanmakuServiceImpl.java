package com.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.entity.Danmaku;
import com.video.mapper.DanmakuMapper;
import com.video.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanmakuServiceImpl implements DanmakuService {

    @Autowired
    private DanmakuMapper danmakuMapper;

    @Override
    public List<Danmaku> getDanmakuByVideoId(Long videoId) {
        LambdaQueryWrapper<Danmaku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Danmaku::getVideoId, videoId).orderByAsc(Danmaku::getVideoTime);
        return danmakuMapper.selectList(wrapper);
    }

    @Override
    public void addDanmaku(Danmaku danmaku) { danmakuMapper.insert(danmaku); }
}