package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import com.example.java_tweets.models.dtos.request.TweetPostCommentDTO;
import com.example.java_tweets.models.dtos.request.TweetPostTweetDTO;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.repositorys.TweetRepository;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/post-tweet")
    public ResponseEntity<Object> postTweet(@RequestBody TweetPostTweetDTO tweetPostTweetDTO) {
        User targetUser = userRepository.findById(tweetPostTweetDTO.getUserId()).orElse(null);

        if (targetUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user");
        }

        Tweet newTweet = new Tweet();
        newTweet.setTweetOwner(targetUser);
        newTweet.setTitle(tweetPostTweetDTO.getTitle());
        newTweet.setContent(tweetPostTweetDTO.getContent());
        newTweet.setOwnerName(targetUser.getName());
        tweetRepository.save(newTweet);
        return ResponseEntity.ok("Tweeted");
    }

    @GetMapping("/find-all")
    public @ResponseBody Iterable<Tweet> findAllTweets() {
        return tweetRepository.findAll();
    }

    @GetMapping("/find-by-user")
    public @ResponseBody Iterable<Tweet> findTweetsByUser(@RequestParam Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            return new ArrayList<>();
        }

        return tweetRepository.findByTweetOwner(targetUser);
    }

    @GetMapping("/find-by-friends")
    public @ResponseBody Iterable<Tweet> findByFriendsTweets(@RequestParam Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        List<Tweet> friendsTweets = new ArrayList<>();
        List<Integer> targetFriendIds = new ArrayList<>();

        if (targetUser == null) {
            return new ArrayList<>();
        }
        for (User user : targetUser.getFriends()) {
            targetFriendIds.add(user.getId());
        }
        for (Integer user : targetFriendIds) {
            for (Tweet tweet : findTweetsByUser(user)) {
                friendsTweets.add(tweet);
            }
        }

        return friendsTweets;
    }



    @PostMapping
    public @ResponseBody String likeTweet(@RequestParam Integer tweetId, @RequestParam Integer userId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetTweet == null || targetUser == null) {
            return "No such tweet or user";
        }

        targetTweet.setLike(userId);
        tweetRepository.save(targetTweet);

        return "Tweet liked";
    }

    @DeleteMapping()
    public @ResponseBody String deleteTweet(@RequestParam Integer tweetId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            return "No such tweet";
        }

        tweetRepository.delete(targetTweet);

        return "Tweet deleted";
    }

    @GetMapping("/comments")
    public @ResponseBody Iterable<Comment> getComments(@RequestParam Integer tweetId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            return new ArrayList<>();
        }

        return targetTweet.getTweetComments();
    }

    @PostMapping("/comments")
    public ResponseEntity<Object> addComment(@RequestBody TweetPostCommentDTO tweetPostCommentDTO) {
        Tweet targetTweet = tweetRepository.findById(tweetPostCommentDTO.getTweetId()).orElse(null);
        User commenter = userRepository.findById(tweetPostCommentDTO.getUserId()).orElse(null);

        if (targetTweet == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid tweet");
        }

        if (commenter == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user");
        }

        Comment comment = new Comment();
        comment.setCommentOwner(commenter);
        comment.setContent(tweetPostCommentDTO.getContent());
        comment.setOnTweet(targetTweet);
        comment.setOwnerName(commenter.getName());

        commentRepository.save(comment);
        targetTweet.addNewComment(comment);
        tweetRepository.save(targetTweet);

        return ResponseEntity.ok("Commented.");
    }

    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<Tweet> tweetList = tweetRepository.findAll();

        tweetRepository.deleteAll(tweetList);
        return "All tweets deleted";
    }

}
