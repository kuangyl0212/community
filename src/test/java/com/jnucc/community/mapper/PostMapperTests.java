package com.jnucc.community.mapper;

import com.jnucc.community.CommunityApplication;
import com.jnucc.community.entity.Post;
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
public class PostMapperTests {
    @Autowired
    private PostMapper postMapper;

    @Test
    public void testSelect() {
        List<Post> list = postMapper.selectByUserId(0, 0, 10);
        for (Post post : list) {
            System.out.println(post.getTitle());
        }
    }

    @Test
    public void testTotalRows() {
        System.out.println(postMapper.totalRows(0));
    }

    @Test
    public void testCreatePost() {
        int userId = 164;
        String title = "test";
        String content = "test content";
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        postMapper.insert(post);
    }
}
