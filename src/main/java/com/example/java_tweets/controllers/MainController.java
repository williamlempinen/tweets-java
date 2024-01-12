package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import com.example.java_tweets.repositorys.TweetRepository;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
/**
 * ######################################################
 * #########   NOT IN USE    ############################
 * ######################################################
 */

@Controller
@RequestMapping()
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @PostMapping("/create-new-user")
    public @ResponseBody String createNewUser(@RequestParam String name, @RequestParam String email) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        userRepository.save(newUser);
        return "New user created!";
    }

    @GetMapping("/find-all-users")
    public @ResponseBody Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

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

    @GetMapping("/find-all-tweets")
    public @ResponseBody Iterable<Tweet> findAllTweets() {
        return tweetRepository.findAll();
    }

    @GetMapping("/find-tweet/by-user/{userId}")
    public @ResponseBody Iterable<Tweet> findTweetsByUser(@PathVariable Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            //bad way to solve this!!!
            return new ArrayList<>();
        }

        return tweetRepository.findByTweetOwner(targetUser);
    }

    //get comments
    //like comment
    //add comment
    //remove comment


    //like tweet
    //remove tweet

    //add user as a friend
    //remove user from friends

}
