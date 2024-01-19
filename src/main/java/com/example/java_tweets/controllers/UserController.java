package com.example.java_tweets.controllers;

import com.example.java_tweets.models.*;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ########   ALL ENDPOINTS
 * ###############################################
 * ########   POST /login
 * ########   POST /create-user
 * ########   GET /find-all
 * ########   POST /{userId}
 * ########   DELETE /{userId}
 * ########   GET /{userId}/friends
 * ###############################################
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO user) {
        User targetUser = userRepository.findByEmail(user.getEmail());

        if (targetUser != null && targetUser.getPassword().equals(user.getPassword())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(targetUser.getId());
            userDTO.setName(targetUser.getName());
            userDTO.setEmail(targetUser.getEmail());
            userDTO.setFriendsList(targetUser.getFriends());
            userDTO.setCommentList(targetUser.getCommentList());
            userDTO.setTweetList(targetUser.getTweetList());
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

    @PostMapping("/create-user")
    public @ResponseBody String createNewUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(password);
        userRepository.save(newUser);

        return "New user created!";
    }

    @GetMapping("/find-all")
    public @ResponseBody Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    //@GetMapping("/find-all-friends")
    //public @ResponseBody Iterable<UserFriend> findAllFriends() {
        //return userFriendRepository.findAll();
    //}

    @GetMapping("/find-all-friends")
    public @ResponseBody Iterable<String> findAllUsersFriends() {
        Iterable<User> allUsers = userRepository.findAll();
        List<String> allUserFriends = new ArrayList<>();

        for (User user : allUsers) {
            allUserFriends.addAll(user.getFriends());
        }
        return allUserFriends;
    }

    @PostMapping("/")
    public @ResponseBody String addFriend(@RequestParam Integer userId, @RequestParam Integer friendUserId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        User friendUser = userRepository.findById(friendUserId).orElse(null);

        if (targetUser == null || friendUser == null) {
            return "User not found";
        }

        for (String userFriend : targetUser.getFriends()) {
            if (userFriend.substring(userFriend.indexOf("id=") + 3, userFriend.indexOf(",")).equals(friendUser.getId().toString())) {
                return "Already friends";
            }
        }

        UserFriend targetFriendUser = new UserFriend(targetUser.getId(), targetUser.getName(), targetUser.getEmail());
        UserFriend friendFriendUser = new UserFriend(friendUser.getId(), friendUser.getName(), friendUser.getEmail());
        targetUser.getFriends().add(friendFriendUser.toString());
        friendUser.getFriends().add(targetFriendUser.toString());

        userRepository.save(friendUser);
        userRepository.save(targetUser);

        return "You are now friends";
    }

    private boolean removeUserFriendString(List<String> userFriends, Integer friendUserId) {
        for (String friendString : userFriends) {
            String idString = friendString.substring(friendString.indexOf("id=") + 3, friendString.indexOf(","));
            if (idString.equals(friendUserId.toString())) {
                userFriends.remove(friendString);
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("/")
    public @ResponseBody String removeFriend(@RequestParam Integer userId, @RequestParam Integer friendUserId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        User friendUser = userRepository.findById(friendUserId).orElse(null);

        if (targetUser == null || friendUser == null) {
            return "User not found";
        }
        boolean removedFromTarget = removeUserFriendString(targetUser.getFriends(), friendUserId);
        boolean removedFromFriend = removeUserFriendString(friendUser.getFriends(), userId);

        if (!removedFromTarget || !removedFromFriend) {
            return "Not friends, so can't remove";
        }
        userRepository.save(friendUser);
        userRepository.save(targetUser);

        return "You are not friends anymore";
    }

    @GetMapping("/friends")
    public @ResponseBody Iterable<String> getFriends(@RequestParam Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            return new ArrayList<>();
        }

        return targetUser.getFriends();
    }

    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<User> userList = userRepository.findAll();
        userRepository.deleteAll(userList);

        return "All users deleted";
    }

}
