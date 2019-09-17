package com.jnucc.community.mapper;


import com.jnucc.community.CommunityApplication;
import com.jnucc.community.entity.Comment;
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
public class CommentMapperTests {
    @Autowired
    private CommentMapper mapper;

    @Test
    public void testSelectByEntity() {
        List<Comment> comments = mapper.selectByEntity(1, 231, 0, 10);
        for (Comment comment : comments) {
            System.out.println(comment);
        }
        System.out.println(mapper.rowsOfEntity(1, 231));
        List<Comment> comments1 = mapper.selectAllByEntity(1, 231);
        for (Comment comment : comments1) {
            System.out.println(comment);
        }
    }
}
