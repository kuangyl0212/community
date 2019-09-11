package com.jnucc.community.service;

import com.jnucc.community.CommunityApplication;
import com.jnucc.community.service.HomeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class HomeServiceTests {
    @Autowired
    private HomeService homeService;

    @Test
    public void testGetPosts() {
        homeService.getPosts(0,10);
    }

    @Test
    public void testGetTotalRows() {
        homeService.getTotalRows();
    }
}
