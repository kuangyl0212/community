package com.jnucc.community.service;

import com.jnucc.community.mapper.PostMapper;
import com.jnucc.community.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {
    @Autowired
    private PostMapper postMapper;

    public List<Post> getPosts(int offset, int limit) {
        return postMapper.selectByUserId(0, offset, limit);
    }

    public int getTotalRows() {
        return postMapper.totalRows(0);
    }
}
