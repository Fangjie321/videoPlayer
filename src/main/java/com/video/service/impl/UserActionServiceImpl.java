package com.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.entity.UserAction;
import com.video.mapper.UserActionMapper;
import com.video.mapper.VideoMapper;
import com.video.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserActionServiceImpl implements UserActionService {

    @Autowired
    private UserActionMapper userActionMapper;

    @Autowired
    private VideoMapper videoMapper;

    private static final int ACTION_TYPE_LIKE = 1;
    private static final int ACTION_TYPE_COLLECT = 2;

    @Override
    @Transactional
    public boolean likeVideo(Long userId, Long videoId) {
        LambdaQueryWrapper<UserAction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAction::getUserId, userId)
                .eq(UserAction::getVideoId, videoId)
                .eq(UserAction::getActionType, ACTION_TYPE_LIKE);

        UserAction existing = userActionMapper.selectOne(wrapper);

        if (existing != null) {
            userActionMapper.deleteById(existing.getId());
            videoMapper.decrementLikeCount(videoId);
            return false;
        } else {
            UserAction action = new UserAction();
            action.setUserId(userId);
            action.setVideoId(videoId);
            action.setActionType(ACTION_TYPE_LIKE);
            userActionMapper.insert(action);
            videoMapper.incrementLikeCount(videoId);
            return true;
        }
    }

    @Override
    @Transactional
    public boolean collectVideo(Long userId, Long videoId) {
        LambdaQueryWrapper<UserAction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAction::getUserId, userId)
                .eq(UserAction::getVideoId, videoId)
                .eq(UserAction::getActionType, ACTION_TYPE_COLLECT);

        UserAction existing = userActionMapper.selectOne(wrapper);

        if (existing != null) {
            userActionMapper.deleteById(existing.getId());
            videoMapper.decrementCollectCount(videoId);
            return false;
        } else {
            UserAction action = new UserAction();
            action.setUserId(userId);
            action.setVideoId(videoId);
            action.setActionType(ACTION_TYPE_COLLECT);
            userActionMapper.insert(action);
            videoMapper.incrementCollectCount(videoId);
            return true;
        }
    }

    @Override
    public boolean isLiked(Long userId, Long videoId) {
        LambdaQueryWrapper<UserAction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAction::getUserId, userId)
                .eq(UserAction::getVideoId, videoId)
                .eq(UserAction::getActionType, ACTION_TYPE_LIKE);
        return userActionMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean isCollected(Long userId, Long videoId) {
        LambdaQueryWrapper<UserAction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAction::getUserId, userId)
                .eq(UserAction::getVideoId, videoId)
                .eq(UserAction::getActionType, ACTION_TYPE_COLLECT);
        return userActionMapper.selectCount(wrapper) > 0;
    }
}