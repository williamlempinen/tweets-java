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
 * ########   GET /find-all-friends, just for testing
 * ########   POST                 , addFriend()
 * ########   DELETE               , removeFriend()
 * ########   GET /friends         , getFriends()
 * ########   DELETE /delete-all
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
        List<UserDTO> targetUserFriends = new ArrayList<>();

        for (User friend : targetUser.getFriends()) {
            UserDTO friendDTO = new UserDTO();
            friendDTO.setId(friend.getId());
            friendDTO.setName(friend.getName());
            friendDTO.setEmail(friend.getEmail());
            targetUserFriends.add(friendDTO);
        }

        if (targetUser.getPassword().equals(user.getPassword())) {
            UserDTO userDTO = User.convertToDTO(targetUser);
            userDTO.getFriendsList().addAll(targetUserFriends);
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

    @GetMapping("/find-all-dtos")
    public @ResponseBody Iterable<UserDTO> findAllUserDTOs() {
        Iterable<User> allUsers =  userRepository.findAll();
        List<UserDTO> allUserDTOs = new ArrayList<>();

        for (User user : allUsers) {
            allUserDTOs.add(User.convertToDTO(user));
        }
        return allUserDTOs;
    }


    //endpoint is to find all users that are friends for some user
    @GetMapping("/find-all-friends")
    public @ResponseBody Iterable<UserDTO> findAllUsersFriends() {
        Iterable<User> allUsers = userRepository.findAll();
        List<UserDTO> allUserFriends = new ArrayList<>();

        for (User user : allUsers) {
            for (User friend : user.getFriends()) {
                allUserFriends.add(User.convertToDTO(friend));
            }
        }
        System.err.println(allUserFriends);
        return allUserFriends;
    }

    @PostMapping()
    public @ResponseBody String addFriend(@RequestParam Integer userId, @RequestParam Integer friendUserId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        User friendUser = userRepository.findById(friendUserId).orElse(null);

        if (targetUser == null || friendUser == null) {
            return "User not found";
        }

        if (targetUser.getFriends().contains(friendUser)) {
            return "Already friends";
        }

        targetUser.getFriends().add(friendUser);
        friendUser.getFriends().add(targetUser);

        userRepository.save(friendUser);
        userRepository.save(targetUser);

        return "You are now friends";
    }

    @DeleteMapping()
    public @ResponseBody String removeFriend(@RequestParam Integer userId, @RequestParam Integer friendUserId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        User friendUser = userRepository.findById(friendUserId).orElse(null);

        if (targetUser == null || friendUser == null) {
            return "User not found";
        }

        targetUser.getFriends().remove(friendUser);
        friendUser.getFriends().remove(targetUser);
        userRepository.save(friendUser);
        userRepository.save(targetUser);

        return "You are not friends anymore";
    }

    @GetMapping("/friends")
    public @ResponseBody Iterable<UserDTO> getFriends(@RequestParam Integer userId) {
        User targetUser = userRepository.findById(userId).orElse(null);
        List<UserDTO> userDTOS = new ArrayList<>();

        if (targetUser == null) {
            return new ArrayList<>();
        }

        for (User user : targetUser.getFriends()) {
            userDTOS.add(User.convertToDTO(user));
        }

        return userDTOS;
    }

    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<User> userList = userRepository.findAll();
        userRepository.deleteAll(userList);

        return "All users deleted";
    }

}
