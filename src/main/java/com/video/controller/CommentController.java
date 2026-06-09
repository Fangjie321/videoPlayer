package com.video.controller;

import com.video.common.Result;
import com.video.entity.Comment;
import com.video.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list/{videoId}")
    public Result<List<Comment>> getCommentList(@PathVariable Long videoId) {
        return Result.success(commentService.getCommentsByVideoId(videoId));
    }

    @PostMapping("/add")
    public Result<Void> addComment(@RequestBody Comment comment, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        commentService.addComment(comment);
        return Result.success("评论成功", null);
    }
}