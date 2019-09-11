package com.jnucc.community.service;

import com.jnucc.community.dao.UserMapper;
import com.jnucc.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findByUserId(int userId) {
        return userMapper.selectById(userId);
    }
}
