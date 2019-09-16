package com.jnucc.community.controller;

import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class UtilController {

    @Autowired
    private Producer captchaProducer;

    private Logger logger = LoggerFactory.getLogger(UtilController.class);

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)

    public void getCaptcha(HttpServletResponse response, HttpSession session) {
        String text = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(text);

        session.setAttribute("captcha", text);
        response.setContentType("image/png");

        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("get captcha failed: " + e.getMessage());
        }
    }
}
