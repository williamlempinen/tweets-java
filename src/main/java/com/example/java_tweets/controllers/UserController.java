package com.example.java_tweets.controllers;

import com.example.java_tweets.models.User;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ############################################################################
 * ############################################################################
 * #######    CREATE BETTER ERROR HANDLING    #################################
 * ############################################################################
 * ############################################################################
 *
 */

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create-user")
    public @ResponseBody String createNewUser(@RequestParam String name, @RequestParam String email) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        userRepository.save(newUser);
        return "New user created!";
    }

    @GetMapping("/find-all")
    public @ResponseBody Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/{userId}")
    public @ResponseBody String addFriend(@PathVariable Integer userId, @RequestParam Integer friendUserId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        User friendUser = userRepository.findById(friendUserId).orElse(null);

        if (targetUser == null || friendUser == null) {
            return "User not found";
        }

        List<User> targetFriendList = targetUser.getFriends();
        List<User> friendFriendList = friendUser.getFriends();

        if (targetFriendList.contains(friendUser) || friendFriendList.contains(targetUser)) {
            return "Already friends";
        }

        targetFriendList.add(friendUser);
        friendFriendList.add(targetUser);

        userRepository.save(friendUser);
        userRepository.save(targetUser);

        return "You are now friends";
    }

    @DeleteMapping("/{userId}")
    public @ResponseBody String removeFriend(@PathVariable Integer userId, @RequestParam Integer friendUserId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        User friendUser = userRepository.findById(friendUserId).orElse(null);

        if (targetUser == null || friendUser == null) {
            return "User not found";
        }

        List<User> targetFriendList = targetUser.getFriends();
        List<User> friendFriendList = friendUser.getFriends();

        if (!(targetFriendList.contains(friendUser) || friendFriendList.contains(targetUser))) {
            return "No such friend";
        }

        targetFriendList.remove(friendUser);
        friendFriendList.remove(targetUser);

        userRepository.save(friendUser);
        userRepository.save(targetUser);

        return "You are not friends anymore";
    }

    @GetMapping("/{userId}/friends")
    public @ResponseBody Iterable<User> getFriends(@PathVariable Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            return new ArrayList<>();
        }

        return targetUser.getFriends();
    }

}
