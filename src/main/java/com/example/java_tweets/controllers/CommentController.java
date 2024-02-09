package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.User;
import com.example.java_tweets.models.dtos.request.CommentEventDTO;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.repositorys.TweetRepository;
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

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    public CommentController(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @PostMapping()
    public @ResponseBody String likeComment(@RequestBody CommentEventDTO commentEventDTO) {
        Comment targetComment = commentRepository.findById(commentEventDTO.getCommentId()).orElse(null);
        User targetUser = userRepository.findById(commentEventDTO.getUserId()).orElse(null);

        if (targetComment == null || targetUser == null) {
            return "No such comment or user";
        }

        targetComment.setLike(commentEventDTO.getUserId());
        commentRepository.save(targetComment);

        return "Comment liked";
    }

    //not tested
    @DeleteMapping()
    public @ResponseBody String deleteComment(@RequestBody CommentEventDTO commentEventDTO) {
        Comment targetComment = commentRepository.findById(commentEventDTO.getCommentId()).orElse(null);

        if (targetComment == null) {
            return "No such comment";
        }

        if (Objects.equals(targetComment.getCommentOwner().getId(), commentEventDTO.getUserId()) || Objects.equals(targetComment.getOnTweet().getTweetOwner().getId(), commentEventDTO.getUserId())) {
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
