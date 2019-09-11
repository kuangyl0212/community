package com.jnucc.community.mapper;

import com.jnucc.community.CommunityApplication;
import com.jnucc.community.dao.DiscussPostMapper;
import com.jnucc.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostMapperTests {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelect() {
        List<DiscussPost> list = discussPostMapper.selectByUserId(0, 0, 10);
        for (DiscussPost post : list) {
            System.out.println(post.getTitle());
        }
    }

    @Test
    public void testTotalRows() {
        System.out.println(discussPostMapper.totalRows(0));
    }
}
