package com.jnucc.community.controller;

import com.jnucc.community.entity.Comment;
import com.jnucc.community.service.CommentService;
import com.jnucc.community.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/create/{postId}", method = RequestMethod.POST)
    public String createComment(@PathVariable("postId") int postId, Comment comment) {

        int userId = userHolder.getUser().getId();
        comment.setUserId(userId);

        commentService.createComment(comment);

        return "redirect:/post/detail/" + postId;
    }
}
