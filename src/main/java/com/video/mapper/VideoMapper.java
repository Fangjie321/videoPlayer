package com.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @Update("UPDATE tb_video SET view_count = view_count + 1 WHERE id = #{videoId}")
    int incrementViewCount(@Param("videoId") Long videoId);

    @Update("UPDATE tb_video SET like_count = like_count + 1 WHERE id = #{videoId}")
    int incrementLikeCount(@Param("videoId") Long videoId);

    @Update("UPDATE tb_video SET like_count = like_count - 1 WHERE id = #{videoId}")
    int decrementLikeCount(@Param("videoId") Long videoId);

    @Update("UPDATE tb_video SET collect_count = collect_count + 1 WHERE id = #{videoId}")
    int incrementCollectCount(@Param("videoId") Long videoId);

    @Update("UPDATE tb_video SET collect_count = collect_count - 1 WHERE id = #{videoId}")
    int decrementCollectCount(@Param("videoId") Long videoId);

    @Update("UPDATE tb_video SET comment_count = comment_count + 1 WHERE id = #{videoId}")
    int incrementCommentCount(@Param("videoId") Long videoId);
}