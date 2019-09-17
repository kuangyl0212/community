package com.jnucc.community.service;

import com.jnucc.community.mapper.CommentMapper;
import com.jnucc.community.entity.Comment;
import com.jnucc.community.util.WordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

import static com.jnucc.community.util.constant.Config.*;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private WordFilter wordFilter;

    public List<Comment> findByPostId(int PostId, int offset, int limit) {
        return commentMapper.selectByEntity(POST_ENTITY_TYPE, PostId, offset, limit);
    }

    public List<Comment> findReplyOfComment(int commentId) {
        return commentMapper.selectAllByEntity(COMMENT_ENTITY_TYPE, commentId);
    }

    public int rowsCommentOfPost(int postId) {
        return commentMapper.rowsOfEntity(POST_ENTITY_TYPE, postId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int createComment(Comment comment) {

        if (comment == null) {
            throw new IllegalArgumentException("comment empty");
        }

        String content = comment.getContent();
        content = HtmlUtils.htmlEscape(content);
        content = wordFilter.filter(content);

        comment.setContent(content);

        comment.setStatus(0);
        comment.setCreateTime(new Date());

        int rows = commentMapper.insert(comment);

        if (comment.getEntityType() == POST_ENTITY_TYPE) {
            updateCommentCountOfPost(comment.getEntityId());
        }

        return rows;
    }

    private void updateCommentCountOfPost(int postId) {
        int count = rowsCommentOfPost(postId);
        postService.updateCommentCount(postId, count);
    }
}
