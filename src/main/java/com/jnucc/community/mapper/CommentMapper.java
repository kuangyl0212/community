package com.jnucc.community.mapper;

import com.jnucc.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    Comment selectById(int id);
    List<Comment> selectByUserId(int userId);
    List<Comment> selectByEntity(int entityType, int entityId, int offset, int limit);
    List<Comment> selectAllByEntity(int entityType, int entityId);
    int rowsOfEntity(int entityType, int entityId);
    int insert(Comment comment);
}
