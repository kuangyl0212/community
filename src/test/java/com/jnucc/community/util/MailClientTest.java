package com.jnucc.community.util;


import com.jnucc.community.CommunityApplication;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailClientTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMailSending() {
        mailClient.sendMail("1067877278@qq.com", "TEST", "TEST CONTENT");
    }

    @Test
    public void testHtmlMailSending() {
        Context context = new Context();
        context.setVariable("to", "1067877278@qq.com");
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail("1067877278@qq.com", "TEST HTML", content);
    }
}
