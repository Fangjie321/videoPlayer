package com.video.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_user_action")
public class UserAction {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long videoId;
    private Integer actionType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}