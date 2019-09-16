package com.jnucc.community.util;

import com.jnucc.community.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class WordFilterTests {

    @Autowired
    private WordFilter filter;

    @Test
    public void testFiter() {
        String s1 = "我要吸毒，我要赌博！";
        String s2 = "我要吸￥毒，我要赌￥博！";
        String expected = "我要***，我要***！";
        assertEquals(expected, filter.filter(s1));
        assertEquals(expected, filter.filter(s2));
    }
}
