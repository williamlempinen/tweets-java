package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.repositorys.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * ########   ALL ENDPOINTS
 * ###############################################
 * ########   POST /{commentId}
 * ########   DELETE /{commentId}
 * ###############################################
 */

@Controller
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    //not tested
    @PostMapping("/{commentId}")
    public @ResponseBody String likeComment(@PathVariable Integer commentId) {
        Comment targetComment = commentRepository.findById(commentId).orElse(null);

        if (targetComment == null) {
            return "No such comment";
        }

        targetComment.like();
        commentRepository.save(targetComment);

        return "Comment liked";
    }

    //not tested
    @DeleteMapping("/{commentId}")
    public @ResponseBody String deleteComment(@PathVariable Integer commentId, @RequestParam Integer deleterId) {
        Comment targetComment = commentRepository.findById(commentId).orElse(null);

        if (targetComment == null) {
            return "No such comment";
        }

        if (Objects.equals(targetComment.getCommentOwner().getId(), deleterId) || Objects.equals(targetComment.getOnTweet().getTweetOwner().getId(), deleterId)) {
            commentRepository.delete(targetComment);
            return "Comment deleted";
        }
        return "Something went wrong";
    }

}
