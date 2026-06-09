package com.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.entity.Comment;
import com.video.mapper.CommentMapper;
import com.video.mapper.UserMapper;
import com.video.mapper.VideoMapper;
import com.video.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Comment> getCommentsByVideoId(Long videoId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getVideoId, videoId).orderByDesc(Comment::getCreateTime);
        List<Comment> comments = commentMapper.selectList(wrapper);
        comments.forEach(comment -> comment.setUser(userMapper.selectById(comment.getUserId())));
        return comments;
    }

    @Override
    public void addComment(Comment comment) {
        comment.setLikeCount(0L);
        commentMapper.insert(comment);
        videoMapper.incrementCommentCount(comment.getVideoId());
    }
}