package com.jnucc.community.service;

import com.jnucc.community.dao.DiscussPostMapper;
import com.jnucc.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> getPosts(int offset, int limit) {
        return discussPostMapper.selectByUserId(0, offset, limit);
    }

    public int getTotalRows() {
        return discussPostMapper.totalRows(0);
    }
}
