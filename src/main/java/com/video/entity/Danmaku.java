package com.video.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_danmaku")
public class Danmaku {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long videoId;
    private Long userId;
    private String content;
    private Double videoTime;
    private String color;
    private Integer type;
    private Integer fontSize;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}