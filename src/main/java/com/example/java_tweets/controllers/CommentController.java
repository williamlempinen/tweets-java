package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.dtos.request.CommentEventDTO;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.services.CommentService;
import org.springframework.web.bind.annotation.*;

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
    private final CommentService commentService;

    public CommentController(CommentRepository commentRepository,
                             CommentService commentService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @PostMapping()
    public @ResponseBody String likeComment(@RequestBody CommentEventDTO commentEventDTO) {
        commentService.likeComment(commentEventDTO);
        return "Comment liked";
    }

    //not tested
    @DeleteMapping()
    public @ResponseBody String deleteComment(@RequestBody CommentEventDTO commentEventDTO) {
        commentService.deleteComment(commentEventDTO);
        return "Comment deleted";
    }

    //development endpoint
    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<Comment> commentList = commentRepository.findAll();
        commentRepository.deleteAll(commentList);
        return "All comments deleted";
    }

}
