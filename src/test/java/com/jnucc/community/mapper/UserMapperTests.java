package com.jnucc.community.mapper;

import com.jnucc.community.CommunityApplication;
import com.jnucc.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserMapper() {
        User testUser = new User();
        testUser.setUsername("test_user");
        testUser.setEmail("test@test.com");
        testUser.setPassword("123456");
        testUser.setSalt("1234");
        testUser.setHeaderUrl("http://test.com");
        int testId = userMapper.insert(testUser);
        User user = userMapper.selectById(11);
        System.out.println(user);
        userMapper.selectByEmail("nowcoder11@sina.com");
        System.out.println(user);
        user = userMapper.selectByUsername("nowcoder11");
        System.out.println(user);
    }
}
