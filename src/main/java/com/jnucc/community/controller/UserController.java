package com.jnucc.community.controller;

import com.jnucc.community.constant.Msg;
import com.jnucc.community.constant.UserState;
import com.jnucc.community.entity.User;
import com.jnucc.community.service.UserService;
import com.jnucc.community.util.CommunityUtil;
import com.jnucc.community.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Map;

import static com.jnucc.community.constant.Config.DEFAULT_EXPIRED;
import static com.jnucc.community.constant.Config.REMEMBER_EXPIRED;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserHolder userHolder;

    @Value("${community.upload}")
    private String uploadPath;

    @Value("${community.domain}")
    private String domain;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

//    @Value("{server.servlet")

    @RequestMapping(value = "register", method = RequestMethod.GET)
    String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    String doRegister(Model model, User user) throws IllegalArgumentException {
        String viewName = "/site/register";
        Map<String, Object> registerFeedback = userService.register(user);
        UserState state = (UserState) registerFeedback.get("state");
        switch (state) {
            case OK:
                int userId = (int)registerFeedback.get("userId");
                User userEntity = userService.findByUserId(userId);
                String activationCode = userEntity.getActivationCode();
                viewName = "/site/operate-result";
                model.addAttribute("target", "/index");
                model.addAttribute("redirect_msg", Msg.userRegisterSuccess);
                break;
            case USERNAME_EXIT:
                model.addAttribute("usernameMsg", state.getMsg());
                break;
            case EMAIL_EXIT:
                model.addAttribute("emailMsg", state.getMsg());
                break;
            // TODO other state should be handle
        }
        return viewName;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(Model model, User user, String captcha, boolean remember_me, HttpSession session, HttpServletResponse response) throws IllegalArgumentException {
        String viewName = "/site/login";

        if (session.getAttribute("captcha") == null
                || StringUtils.isBlank(captcha)
                || !captcha.equalsIgnoreCase((String)session.getAttribute("captcha"))) {
            model.addAttribute("captchaMsg", "invalid captcha");
            return viewName;
        }
        int expiredSeconds = remember_me ? REMEMBER_EXPIRED : DEFAULT_EXPIRED;
        Map<String, Object> loginFeedback = userService.login(user, expiredSeconds);
        UserState state = (UserState) loginFeedback.get("state");
        switch (state) {
            case OK:
                String ticket = (String)loginFeedback.get("ticket");
                Cookie cookie = new Cookie("ticket", ticket);
                cookie.setPath("/");
                cookie.setMaxAge(expiredSeconds);
                response.addCookie(cookie);
                viewName = "redirect:/index";
                break;
            case USERNAME_BLANK:
            case USERNAME_NOT_EXIT:
                model.addAttribute("usernameMsg", state.getMsg());
                break;
            case PASSWORD_BLANK:
            case WRONG_PASSWORD:
                model.addAttribute("passwordMsg", state.getMsg());
                break;
            // TODO other state should be handle
        }
        return viewName;
    }

    @RequestMapping(value = "/activate/{userId}/{activationCode}", method = RequestMethod.GET)
    public String activate(Model model, @PathVariable int userId, @PathVariable String activationCode) {
        UserState state = userService.activate(userId, activationCode);
        String viewName = "/site/operate-result";
        switch (state) {
            case OK:
                viewName = "/site/login";
            case ID_NOT_EXIT:
            case ACT_CODE_WRONG:
                model.addAttribute("target", "/index");
                model.addAttribute("redirect_msg", Msg.ActivationFailure + state.getMsg());
                break;
        }
        return viewName;
    }

    @RequestMapping(value = "logout")
    public String doLogout(@CookieValue("ticket") String ticket) {
        userService.destroyTicket(ticket);
        return "redirect:/user/login";
    }

    @RequestMapping(value = "setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String uploadHeaderImage(MultipartFile headerImage, Model model) throws IOException {
        if (headerImage == null) {
            model.addAttribute("uploadError", "file empty");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("uploadError", "invalid image format");
            return "/site/setting";
        }
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + fileName);
        headerImage.transferTo(dest);

        User user = userHolder.getUser();
        String headerUrl = domain + "/user/header/" + fileName;
        userService.updateHeaderUrl(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(value = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response)  {
        String filePath = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf('.'));

        response.setContentType("image/" + suffix);
        try (
                FileInputStream inputStream = new FileInputStream(filePath);
                OutputStream outputStream = response.getOutputStream();
                )
        {

            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.debug("get header failed: " + e.getMessage());
        }
    }
}
