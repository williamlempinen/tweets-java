package com.example.java_tweets.controllers;

import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import com.example.java_tweets.repositorys.TweetRepository;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
        newTweet.setOwner(targetUser);
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

        return tweetRepository.findByOwner(targetUser);
    }

}
