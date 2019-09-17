package com.jnucc.community.controller;

import com.jnucc.community.entity.Comment;
import com.jnucc.community.entity.Post;
import com.jnucc.community.entity.Page;
import com.jnucc.community.entity.User;
import com.jnucc.community.service.CommentService;
import com.jnucc.community.service.PostService;
import com.jnucc.community.service.UserService;
import com.jnucc.community.util.CommunityUtil;
import com.jnucc.community.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

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

    @RequestMapping(value = "/detail/{postId}", method = RequestMethod.GET)
    public String getPostDetail(@PathVariable("postId") int postId, Model model, Page page) {
        Post post = postService.findById(postId);
        if (post != null) {

            User author = userService.findByUserId(post.getUserId());
            page.setPath("/post/detail/" + postId);
            page.setRows(commentService.rowsCommentOfPost(postId));
            List<Comment> comments = commentService.findByPostId(post.getId(), page.getOffset(), page.getLimit());

            ArrayList<HashMap<String, Object>> commentList = new ArrayList<>();

            for (Comment comment : comments) {
                HashMap<String, Object> oneComment = new HashMap<>();
                User commentUser = userService.findByUserId(comment.getUserId());

                oneComment.put("comment", comment);
                oneComment.put("commentUser", commentUser);

                List<Comment> replies = commentService.findReplyOfComment(comment.getId());

                ArrayList<HashMap<String, Object>> replyList = new ArrayList<>();
                for (Comment reply : replies) {
                    HashMap<String, Object> oneReply = new HashMap<>();
                    User replier = userService.findByUserId(reply.getUserId());
                    User host = userService.findByUserId(reply.getTargetId());
                    oneReply.put("reply", reply);
                    oneReply.put("replier", replier);
                    oneReply.put("host", host);

                    replyList.add(oneReply);
                }

                oneComment.put("replies", replyList);

                commentList.add(oneComment);
            }
            model.addAttribute("post", post);
            model.addAttribute("author", author);
            model.addAttribute("comments", commentList);
            return "/site/post-detail";
        }
        return "/site/error/404";
    }
}
