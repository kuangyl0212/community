package com.jnucc.community.service;

import com.jnucc.community.dao.DiscussPostMapper;
import com.jnucc.community.entity.DiscussPost;
import com.jnucc.community.util.WordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Service
public class PostService {

    @Autowired
    private DiscussPostMapper mapper;

    @Autowired
    private WordFilter wordFilter;

    public int create(int userId, String title, String content) {

        DiscussPost post = new DiscussPost();
        post.setUserId(userId);

        title = HtmlUtils.htmlEscape(title);
        content = HtmlUtils.htmlEscape(content);

        title = wordFilter.filter(title);
        content = wordFilter.filter(content);

        post.setTitle(title);
        post.setContent(content);
        post.setType(0);
        post.setStatus(0);
        post.setCreateTime(new Date());
        post.setCommentCount(0);
        post.setScore(0);

        int rows = mapper.insert(post);
        return rows;
    }
}
