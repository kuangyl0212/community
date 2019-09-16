package com.jnucc.community.controller;

import com.jnucc.community.entity.User;
import com.jnucc.community.service.PostService;
import com.jnucc.community.util.CommunityUtil;
import com.jnucc.community.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Enumeration;

@Controller
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserHolder userHolder;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String createPost(String title, String content) {
        User user = userHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "permission deny");
        }

        if (StringUtils.isBlank(title)) {
            return CommunityUtil.getJSONString(400, "empty title");
        }

        int userId = user.getId();
        postService.create(userId, title, content);

        return CommunityUtil.getJSONString(200, "ok");
    }
}
