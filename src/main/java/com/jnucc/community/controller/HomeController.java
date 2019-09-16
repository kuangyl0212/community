package com.jnucc.community.controller;

import com.jnucc.community.entity.DiscussPost;
import com.jnucc.community.entity.Page;
import com.jnucc.community.entity.User;
import com.jnucc.community.service.HomeService;
import com.jnucc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    String home(Model model, Page page) {
//        page = new Page();
        page.setPath("/index");
        page.setRows(homeService.getTotalRows());
        List<DiscussPost> postList = homeService.getPosts(page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        for (DiscussPost post : postList) {
            Map<String, Object> postAndUser = new HashMap<>();
            User user = userService.findByUserId(post.getUserId());
            postAndUser.put("post", post);
            postAndUser.put("user", user);
            discussPosts.add(postAndUser);
        }
        model.addAttribute("posts", discussPosts);
        return "index";
    }
}
