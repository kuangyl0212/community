package com.jnucc.community.controller;

import com.jnucc.community.constant.Msg;
import com.jnucc.community.constant.UserState;
import com.jnucc.community.entity.User;
import com.jnucc.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.jnucc.community.constant.Config.DEFAULT_EXPIRED;
import static com.jnucc.community.constant.Config.REMEMBER_EXPIRED;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

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
}
