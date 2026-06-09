package com.video.service;

import com.video.entity.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByVideoId(Long videoId);
    void addComment(Comment comment);
}