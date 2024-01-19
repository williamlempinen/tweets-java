package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.repositorys.TweetRepository;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


/**
 * ########   ALL ENDPOINTS
 * ###############################################
 * ########   POST /post-tweet
 * ########   GET /find-all
 * ########   POST /
 * ########   GET /find-by/{userId}
 * ########   DELETE /{tweetId}
 * ########   GET /{tweetId}/comments
 * ########   POST /{tweetId}/comments
 * ###############################################
 */

@Controller
@RequestMapping("/api/tweet")
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/post-tweet")
    public @ResponseBody String postTweet(@RequestParam Integer userId, @RequestParam String content) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            return "User not found";
        }

        Tweet newTweet = new Tweet();
        newTweet.setTweetOwner(targetUser);
        newTweet.setContent(content);
        tweetRepository.save(newTweet);
        return "New tweet posted!";
    }

    @GetMapping("/find-all")
    public @ResponseBody Iterable<Tweet> findAllTweets() {
        return tweetRepository.findAll();
    }

    @GetMapping("/find-by-user/{userId}")
    public @ResponseBody Iterable<Tweet> findTweetsByUser(@PathVariable Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            //bad way to solve this!!!
            return new ArrayList<>();
        }

        return tweetRepository.findByTweetOwner(targetUser);
    }

    @PostMapping
    public @ResponseBody String likeTweet(@RequestParam Integer tweetId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            return "No such tweet";
        }

        targetTweet.like();
        tweetRepository.save(targetTweet);

        return "Tweet liked";
    }

    @DeleteMapping("/{tweetId}")
    public @ResponseBody String deleteTweet(@PathVariable Integer tweetId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            return "No such tweet";
        }

        tweetRepository.delete(targetTweet);

        return "Tweet deleted";
    }

    @GetMapping("/{tweetId}/comments")
    public @ResponseBody Iterable<Comment> getComments(@PathVariable Integer tweetId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            return new ArrayList<>();
        }

        return targetTweet.getTweetComments();
    }

    @PostMapping("/{tweetId}/comments")
    public @ResponseBody String addComment(@PathVariable Integer tweetId, @RequestParam Integer userId, @RequestParam String content) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);
        User commenter = userRepository.findById(userId).orElse(null);

        if (targetTweet == null) {
            return "No such tweet found";
        }

        if (commenter == null) {
            return "Commenting user not found";
        }

        Comment comment = new Comment();
        comment.setCommentOwner(commenter);
        comment.setContent(content);
        comment.setOnTweet(targetTweet);

        commentRepository.save(comment);
        targetTweet.addNewComment(comment);
        tweetRepository.save(targetTweet);

        return "New comment added";

    }

    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<Tweet> tweetList = tweetRepository.findAll();

        tweetRepository.deleteAll(tweetList);
        return "All tweets deleted";
    }
    //get comments
    //add comment

}
