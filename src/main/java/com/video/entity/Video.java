package com.video.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_video")
public class Video {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String description;
    private String coverUrl;
    private String videoUrl;
    private Long duration;
    private Long userId;
    private Long categoryId;
    private Long viewCount;
    private Long likeCount;
    private Long collectCount;
    private Long commentCount;
    private Long danmakuCount;
    private Integer status;
    private Integer isHot;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Category category;
}