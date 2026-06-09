package com.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.video.mapper")
public class VideoPlayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoPlayerApplication.class, args);
    }
}