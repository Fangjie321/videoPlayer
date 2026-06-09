package com.video.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long videoId;
    private Long userId;
    private String content;
    private Long likeCount;
    private Long parentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private User user;
}