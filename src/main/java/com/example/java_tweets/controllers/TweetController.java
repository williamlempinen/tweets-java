package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import com.example.java_tweets.models.dtos.request.TweetPostCommentDTO;
import com.example.java_tweets.models.dtos.request.TweetPostLikeDTO;
import com.example.java_tweets.models.dtos.request.TweetPostTweetDTO;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.repositorys.TweetRepository;
import com.example.java_tweets.repositorys.UserRepository;
import com.example.java_tweets.services.TweetService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * ########   ALL ENDPOINTS
 * ###############################################
 * ########   POST /post-tweet
 * ########   GET /search
 * ########   GET /find-all
 * ########   POST              ,like()
 * ########   GET /find-by-user
 * ########   GET /find-by-friends
 * ########   DELETE            , deleteTweet()
 * ########   GET /comments     ,getComments()
 * ########   POST /comments    , addComment()
 * ###############################################
 */

@RestController
@RequestMapping("/api/tweet")
public class TweetController {

    private final TweetRepository tweetRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final TweetService tweetService;

    public TweetController(TweetRepository tweetRepository,
                           UserRepository userRepository,
                           CommentRepository commentRepository,
                           TweetService tweetService) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.tweetService = tweetService;
    }

    @PostMapping("/post-tweet")
    public ResponseEntity<Object> postTweet(@RequestBody TweetPostTweetDTO tweetPostTweetDTO) {
        tweetService.postTweet(tweetPostTweetDTO);
        return ResponseEntity.ok("Tweeted");
    }

    @GetMapping("/search")
    public @ResponseBody Iterable<Tweet> searchTweets(@RequestParam String query,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return tweetService.searchTweets(query, page, size);
    }

    @GetMapping("/find-all")
    public @ResponseBody Iterable<Tweet> findAllTweets(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return tweetService.findAllTweets(page, size);
    }

    @GetMapping("/find-by-user")
    public @ResponseBody Iterable<Tweet> findTweetsByUser(@RequestParam int userId) {
        return tweetService.findTweetsByUser(userId);
    }

    @GetMapping("/find-by-friends")
    public @ResponseBody List<Tweet> findByFriendsTweets(@RequestParam int userId) {
        return tweetService.findByFriendsTweets(userId);
    }



    @PostMapping
    public @ResponseBody String likeTweet(@RequestBody TweetPostLikeDTO tweetPostLikeDTO) {
        Tweet targetTweet = tweetRepository.findById(tweetPostLikeDTO.getTweetId()).orElse(null);
        User targetUser = userRepository.findById(tweetPostLikeDTO.getUserId()).orElse(null);

        if (targetTweet == null || targetUser == null) {
            return "No such tweet or user";
        }

        targetTweet.setLike(tweetPostLikeDTO.getUserId());
        tweetRepository.save(targetTweet);

        return "Tweet liked";
    }
    @Transactional
    @DeleteMapping()
    public @ResponseBody String deleteTweet(@RequestParam int tweetId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            return "No such tweet";
        }

        commentRepository.deleteByOnTweetId(tweetId);
        tweetRepository.delete(targetTweet);

        return "Tweet deleted";
    }

    @GetMapping("/comments")
    public @ResponseBody List<Comment> getComments(@RequestParam int tweetId) {
        return tweetService.getTweetComment(tweetId);
    }

    @PostMapping("/comments")
    public ResponseEntity<Object> addComment(@RequestBody TweetPostCommentDTO tweetPostCommentDTO) {
        tweetService.addComment(tweetPostCommentDTO);
        return ResponseEntity.ok("Commented.");
    }

    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<Tweet> tweetList = tweetRepository.findAll();

        tweetRepository.deleteAll(tweetList);
        return "All tweets deleted";
    }

}
