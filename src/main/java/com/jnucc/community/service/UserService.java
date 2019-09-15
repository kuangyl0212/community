package com.jnucc.community.service;

import com.jnucc.community.constant.UserState;
import com.jnucc.community.dao.LoginTicketMapper;
import com.jnucc.community.dao.UserMapper;
import com.jnucc.community.entity.LoginTicket;
import com.jnucc.community.entity.User;
import com.jnucc.community.util.CommunityUtil;
import com.jnucc.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    LoginTicketMapper ticketMapper;

    @Autowired
    private MailClient mailClient;

    @Value("${community.domain}")
    private String domain;

    @Value("${community.protocol}")
    private String protocol;

    public User findByUserId(int userId) {
        return userMapper.selectById(userId);
    }
    public User findByUsername(String username) {return userMapper.selectByUsername(username);}

    public Map<String, Object> register(User user) throws IllegalArgumentException {
        HashMap<String, Object> feedback = new HashMap<>();

        if (registerCheck(user, feedback)) {
            int userId = doRegister(user);
            feedback.put("userId", userId);
            feedback.put("state", UserState.OK);
        }

        return feedback;
    }

    private boolean registerCheck(User user, Map<String, Object> feedback) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            feedback.put("state", UserState.USERNAME_BLANK);
            return false;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            feedback.put("state", UserState.EMAIL_BLANK);
            return false;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            feedback.put("state", UserState.PASSWORD_BLANK);
            return false;
        }

        if (isUsernameExit(user.getUsername())) {
            feedback.put("state", UserState.USERNAME_EXIT);
            return false;
        }
        if (isEmailExit(user.getEmail())) {
            feedback.put("state", UserState.EMAIL_EXIT);
            return false;
        }
        return true;
    }

    public UserState activate(int userId, String activationCode) {
        User user = userMapper.selectById(userId);
        if (user == null)
            return UserState.ID_NOT_EXIT;
        if (!user.getActivationCode().equals(activationCode))
            return UserState.ACT_CODE_WRONG;
        userMapper.updateStatus(userId, 1);
        return UserState.OK;
    }

    private boolean isUsernameExit(String username) {
        return userMapper.selectByUsername(username) != null;
    }

    private boolean isEmailExit(String email) {
        return userMapper.selectByEmail(email) != null;
    }

    private int doRegister(User user) {
        String password = user.getPassword();
        String salt = CommunityUtil.generateUUID();
        user.setSalt(salt);
        user.setPassword(CommunityUtil.MD5(password + salt));
        String activationCode = CommunityUtil.generateUUID();
        user.setActivationCode(activationCode);
        user.setHeaderUrl(getRandomHeaderUrl());
        user.setCreateTime(new Date());
        int n_rows = userMapper.insert(user);
        int userId = userMapper.selectByUsername(user.getUsername()).getId();
        String email = user.getEmail();
        sendActivationEmail(email, userId, activationCode);
        return userId;
    }

    private String getRandomHeaderUrl() {
        return String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
    }

    private void sendActivationEmail(String email, int userId, String activationCode) {
        String activationUrl = String.format(
                "%s://%s/user/activate/%d/%s",
                protocol, domain, userId, activationCode);
        Map<String, String> emailPayload = new HashMap<>();
        emailPayload.put("activate_url", activationUrl);
        mailClient.sendMail(email, "Account activation", "/mail/activation", emailPayload);
    }

    public Map<String, Object> login(User user, int expiredSeconds) throws IllegalArgumentException {

        Map<String, Object> feedback = new HashMap<>();
        if (loginCheck(user, feedback)) {
            User found = (User)feedback.get("found");
            doLogin(found, expiredSeconds, feedback);
        }
        return feedback;
    }

    private boolean loginCheck(User user, Map<String, Object> feedback) throws IllegalArgumentException {

        if (user == null) {
            throw new IllegalArgumentException("invalid param");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            feedback.put("state", UserState.USERNAME_BLANK);
            return false;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            feedback.put("state", UserState.PASSWORD_BLANK);
            return false;
        }
        User found = findByUsername(user.getUsername());
        if (found == null) {
            feedback.put("state", UserState.USERNAME_NOT_EXIT);
            return false;
        }
        if (!isActivate(found)) {
            feedback.put("state", UserState.INACTIVATED);
            return false;
        }

        String passwd = CommunityUtil.MD5(user.getPassword() + found.getSalt());
        assert passwd != null;
        if (!passwd.equals(found.getPassword())) {
            feedback.put("state", UserState.WRONG_PASSWORD);
            return false;
        }
        feedback.put("found", found);
        return true;
    }

    private boolean isActivate(User user) {
        return user.getStatus() == 1; // 0 inactivated  | 1 activated
    }

    private void doLogin(User user, int expiredSeconds, Map<String, Object> feedback) {
        String ticket = generateTicket(user.getId(), expiredSeconds);
        feedback.put("ticket", ticket);
        feedback.put("state", UserState.OK);
    }

    private String generateTicket(int userId, int expiredSeconds) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        String text = CommunityUtil.generateUUID();
        ticket.setTicket(text);
        ticket.setStatus(0);
        ticket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        ticketMapper.insertTicket(ticket);
        return text;
    }

    public void destroyTicket(String ticket) {
        ticketMapper.updateStatus(ticket, 1);
    }
}

