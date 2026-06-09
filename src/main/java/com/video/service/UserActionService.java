package com.video.service;

public interface UserActionService {
    boolean likeVideo(Long userId, Long videoId);
    boolean collectVideo(Long userId, Long videoId);
    boolean isLiked(Long userId, Long videoId);
    boolean isCollected(Long userId, Long videoId);
}