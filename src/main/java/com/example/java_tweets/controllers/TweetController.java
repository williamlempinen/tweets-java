package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.dtos.request.TweetPostCommentDTO;
import com.example.java_tweets.models.dtos.request.TweetPostLikeDTO;
import com.example.java_tweets.models.dtos.request.TweetPostTweetDTO;
import com.example.java_tweets.repositorys.TweetRepository;
import com.example.java_tweets.services.TweetService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    private final TweetService tweetService;

    public TweetController(TweetRepository tweetRepository,
                           TweetService tweetService) {
        this.tweetRepository = tweetRepository;
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
                                                      @RequestParam(defaultValue = "5") int size) {
        return tweetService.searchTweets(query, page, size);
    }

    @GetMapping("/find-all")
    public @ResponseBody Iterable<Tweet> findAllTweets(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return tweetService.findAllTweets(page, size);
    }

    @GetMapping("/find-by-user")
    public @ResponseBody List<Tweet> findTweetsByUser(@RequestParam int userId) {
        return tweetService.findTweetsByUser(userId);
    }

    @GetMapping("/find-by-friends")
    public @ResponseBody List<Tweet> findByFriendsTweets(@RequestParam int userId) {
        return tweetService.findByFriendsTweets(userId);
    }

    @PostMapping
    public @ResponseBody String likeTweet(@RequestBody TweetPostLikeDTO tweetPostLikeDTO) {
        tweetService.likeTweet(tweetPostLikeDTO);
        return "Tweet liked";
    }
    @Transactional
    @DeleteMapping()
    public @ResponseBody String deleteTweet(@RequestParam int tweetId) {
        tweetService.deleteTweet(tweetId);
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
