package com.jnucc.community.util;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Min;
import java.util.Map;

@Component
public class MailClient {

    private Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Mail Sending Error: " + e.getMessage());
        }

    }

    public void sendMail(String to, String subject, String template, Map<String, String> payload) {
        Context context = new Context();
        context.setVariable("to", to);
        for (Map.Entry<String, String> entry : payload.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        String content = templateEngine.process(template, context);
        sendMail(to, subject, content);
    }
}
