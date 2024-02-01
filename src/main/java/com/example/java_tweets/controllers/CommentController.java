package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.User;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

/**
 * ########   ALL ENDPOINTS
 * ###############################################
 * ########   POST
 * ########   DELETE
 * ########   DELETE /delete-all
 * ###############################################
 */

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    //not tested
    @PostMapping()
    public @ResponseBody String likeComment(@RequestParam Integer commentId, @RequestParam Integer userId) {
        Comment targetComment = commentRepository.findById(commentId).orElse(null);
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetComment == null || targetUser == null) {
            return "No such comment or user";
        }

        targetComment.setLike(userId);
        commentRepository.save(targetComment);

        return "Comment liked";
    }

    //not tested
    @DeleteMapping()
    public @ResponseBody String deleteComment(@RequestParam Integer commentId, @RequestParam Integer deleterId) {
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

    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<Comment> commentList = commentRepository.findAll();

        commentRepository.deleteAll(commentList);
        return "All comments deleted";
    }

}
