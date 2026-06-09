-- 创建数据库
CREATE DATABASE IF NOT EXISTS video_player DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE video_player;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png' COMMENT '头像',
    `email` VARCHAR(100) COMMENT '邮箱',
    `signature` VARCHAR(255) DEFAULT '这个人很懒，什么都没写' COMMENT '个性签名',
    `level` INT DEFAULT 1 COMMENT '用户等级',
    `coin` INT DEFAULT 0 COMMENT '硬币数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 视频表
CREATE TABLE IF NOT EXISTS `video` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '视频ID',
    `title` VARCHAR(100) NOT NULL COMMENT '视频标题',
    `description` VARCHAR(500) COMMENT '视频描述',
    `cover_url` VARCHAR(255) COMMENT '封面URL',
    `video_url` VARCHAR(255) NOT NULL COMMENT '视频URL',
    `duration` INT DEFAULT 0 COMMENT '视频时长(秒)',
    `user_id` BIGINT NOT NULL COMMENT '上传者ID',
    `category_id` BIGINT COMMENT '分类ID',
    `view_count` BIGINT DEFAULT 0 COMMENT '播放量',
    `like_count` BIGINT DEFAULT 0 COMMENT '点赞数',
    `coin_count` BIGINT DEFAULT 0 COMMENT '投币数',
    `collect_count` BIGINT DEFAULT 0 COMMENT '收藏数',
    `share_count` BIGINT DEFAULT 0 COMMENT '分享数',
    `danmaku_count` BIGINT DEFAULT 0 COMMENT '弹幕数',
    `comment_count` BIGINT DEFAULT 0 COMMENT '评论数',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-审核中 1-已发布 2-已下架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频表';

-- 视频分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频分类表';

-- 弹幕表
CREATE TABLE IF NOT EXISTS `danmaku` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '弹幕ID',
    `video_id` BIGINT NOT NULL COMMENT '视频ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content` VARCHAR(255) NOT NULL COMMENT '弹幕内容',
    `video_time` DECIMAL(10,3) NOT NULL COMMENT '视频时间点(秒)',
    `color` VARCHAR(20) DEFAULT '#FFFFFF' COMMENT '弹幕颜色',
    `type` TINYINT DEFAULT 1 COMMENT '弹幕类型: 1-滚动 2-顶部 3-底部',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_video_id` (`video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='弹幕表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `video_id` BIGINT NOT NULL COMMENT '视频ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID',
    `root_id` BIGINT DEFAULT 0 COMMENT '根评论ID',
    `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX `idx_video_id` (`video_id`),
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 用户点赞/投币/收藏表
CREATE TABLE IF NOT EXISTS `user_action` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `video_id` BIGINT NOT NULL COMMENT '视频ID',
    `action_type` TINYINT NOT NULL COMMENT '动作类型: 1-点赞 2-投币 3-收藏',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_video_action` (`user_id`, `video_id`, `action_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为表';

-- 插入默认分类
INSERT INTO `category` (`name`, `sort`) VALUES
('推荐', 1),
('动画', 2),
('游戏', 3),
('音乐', 4),
('舞蹈', 5),
('影视', 6),
('知识', 7),
('科技', 8),
('生活', 9),
('美食', 10);

-- 插入测试用户
INSERT INTO `user` (`username`, `password`, `nickname`, `signature`, `level`, `coin`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', '欢迎来到视频网站', 6, 1000),
('test', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', '这是一个测试账号', 2, 100);

-- 插入测试视频
INSERT INTO `video` (`title`, `description`, `cover_url`, `video_url`, `duration`, `user_id`, `category_id`, `view_count`, `like_count`, `coin_count`, `collect_count`) VALUES
('Spring Boot教程 - 快速入门', 'Spring Boot入门教程，从零开始学习Spring Boot框架', 'https://picsum.photos/seed/video1/320/180', 'https://www.w3schools.com/html/mov_bbb.mp4', 300, 1, 7, 12580, 856, 234, 567),
('Vue3 完整教程', 'Vue3组合式API详解，带你深入理解Vue3的核心概念', 'https://picsum.photos/seed/video2/320/180', 'https://www.w3schools.com/html/mov_bbb.mp4', 600, 1, 7, 8960, 456, 123, 345),
('前端工程师的一天', '记录前端程序员的真实工作日常', 'https://picsum.photos/seed/video3/320/180', 'https://www.w3schools.com/html/mov_bbb.mp4', 420, 2, 9, 5678, 234, 67, 189),
('程序员必备的VS Code插件', '分享提升开发效率的VS Code插件合集', 'https://picsum.photos/seed/video4/320/180', 'https://www.w3schools.com/html/mov_bbb.mp4', 180, 1, 8, 3456, 189, 45, 123),
('MySQL优化指南', '从索引到查询优化，全面讲解MySQL性能优化', 'https://picsum.photos/seed/video5/320/180', 'https://www.w3schools.com/html/mov_bbb.mp4', 540, 2, 7, 7890, 567, 156, 423);

-- 插入测试弹幕
INSERT INTO `danmaku` (`video_id`, `user_id`, `content`, `video_time`, `color`, `type`) VALUES
(1, 2, '讲的太好了！', 5.5, '#FFFFFF', 1),
(1, 2, '学到很多', 10.2, '#00FF00', 1),
(1, 2, '收藏了', 15.8, '#FFFF00', 1),
(2, 1, 'Vue3真香', 3.2, '#FFFFFF', 1),
(2, 1, '组合式API太好用了', 8.5, '#FF6600', 1);

-- 插入测试评论
INSERT INTO `comment` (`video_id`, `user_id`, `content`, `like_count`) VALUES
(1, 2, '教程很详细，感谢分享！', 56),
(1, 2, '希望能出更多教程', 23),
(2, 1, 'Vue3确实比Vue2好用很多', 34);