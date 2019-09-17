package com.jnucc.community.mapper;

import com.jnucc.community.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> selectByUserId(int userId, int offset, int limit);
    int totalRows(int userId);
    int insert(Post post);

    Post selectById(int postId);

    int updateCommentCount(int postId, int commentCount);
}
