package com.jnucc.community.dao;

import com.jnucc.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    public List<DiscussPost> selectByUserId(int userId, int offset, int limit);
    public int totalRows(int userId);
    public int insert(DiscussPost post);
}
