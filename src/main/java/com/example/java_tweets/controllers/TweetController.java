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
import java.util.Collection;
import java.util.List;


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
    public @ResponseBody String postTweet(@RequestParam Integer userId, @RequestParam String title, @RequestParam String content) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            return "User not found";
        }

        Tweet newTweet = new Tweet();
        newTweet.setTweetOwner(targetUser);
        newTweet.setTitle(title);
        newTweet.setContent(content);
        newTweet.setOwnerName(targetUser.getName());
        tweetRepository.save(newTweet);
        return "New tweet posted!";
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
    public @ResponseBody Iterable<Tweet> findTweetsByFriends(@RequestParam Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            return new ArrayList<>();
        }
        List<Integer> targetFriendIds = new ArrayList<>();
        List<Tweet> tweetsByFriends = new ArrayList<>();

        for (String friend : targetUser.getFriends()) {
            Integer id = Integer.parseInt(friend.substring(friend.indexOf("id=") + 3, friend.indexOf(",")));
            targetFriendIds.add(id);
        }
        for (Integer id : targetFriendIds) {
            tweetsByFriends.addAll((Collection<? extends Tweet>) findTweetsByUser(id));
        }
        return tweetsByFriends;
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
    public @ResponseBody String addComment(@RequestParam Integer tweetId, @RequestParam Integer userId, @RequestParam String content) {
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

}
