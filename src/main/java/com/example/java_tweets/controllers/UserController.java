package com.example.java_tweets.controllers;

import com.example.java_tweets.models.*;
import com.example.java_tweets.models.dtos.request.UserCreateDTO;
import com.example.java_tweets.models.dtos.request.UserFriendStatusDTO;
import com.example.java_tweets.models.dtos.request.UserLoginDTO;
import com.example.java_tweets.models.dtos.response.UserDTO;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ########   ALL ENDPOINTS
 * ###############################################
 * ########   POST /login
 * ########   POST /create-user
 * ########   GET /find-all-info   , testing
 * ########   GET /find-all
 * ########   GET /search
 * ########   GET /find-all-friends, just for testing
 * ########   POST                 , addFriend()
 * ########   DELETE               , removeFriend()
 * ########   GET /friends         , getFriends()
 * ########   DELETE /delete-all
 * ###############################################
 * NOT UPDATED
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
            UserDTO userDTO = User.convertToDTO(targetUser);
            userDTO.setFriendsList(convertUserFriendsToDTOList(targetUser.getFriends()));
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

    @PostMapping("/create-user")
    public ResponseEntity<Object> createNewUser(@RequestBody UserCreateDTO user) {
        try {
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setName(user.getName());
            newUser.setPassword(user.getPassword());
            userRepository.save(newUser);
        } catch (Exception e) {
            System.err.println("Error " + e);
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }

        return ResponseEntity.ok("User created.");
    }

    @GetMapping("/search")
    public @ResponseBody Iterable<UserDTO> searchUsers(@RequestParam String query, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByNameContainingIgnoreCase(query, pageable);

        return userPage.map(User::convertToDTO);
    }

    //this endpoint is only for testing purposes!!
    @GetMapping("/find-all-info")
    public @ResponseBody Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/find-all")
    public @ResponseBody Iterable<UserDTO> findAllUserDTOs() {
        Iterable<User> allUsers =  userRepository.findAll();
        List<UserDTO> allUserDTOs = new ArrayList<>();

        for (User user : allUsers) {
            List<UserDTO> userFriendDTOs = new ArrayList<>();
            UserDTO dto = User.convertToDTO(user);
            for (User userFriend : user.getFriends()) {
                userFriendDTOs.add(User.convertToDTO(userFriend));
            }
            dto.setFriendsList(userFriendDTOs);
            allUserDTOs.add(dto);
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
        return allUserFriends;
    }

    @PostMapping()
    public ResponseEntity<Object> addFriend(@RequestBody @Validated UserFriendStatusDTO userFriendStatusDTO) {
        User targetUser = userRepository.findById(userFriendStatusDTO.getUserId()).orElse(null);
        User friendUser = userRepository.findById(userFriendStatusDTO.getFriendUserId()).orElse(null);

        if (targetUser == null || friendUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (targetUser.getFriends().contains(friendUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already friends");
        }

        targetUser.getFriends().add(friendUser);
        friendUser.getFriends().add(targetUser);

        userRepository.save(friendUser);
        userRepository.save(targetUser);

        UserDTO userDTO = User.convertToDTO(targetUser);
        userDTO.setFriendsList(convertUserFriendsToDTOList(targetUser.getFriends()));

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping()
    public ResponseEntity<Object> removeFriend(@RequestBody UserFriendStatusDTO userFriendStatusDTO) {
        User targetUser = userRepository.findById(userFriendStatusDTO.getUserId()).orElse(null);
        User friendUser = userRepository.findById(userFriendStatusDTO.getFriendUserId()).orElse(null);

        if (targetUser == null || friendUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (!targetUser.getFriends().contains(friendUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Users are not friends and cannot be removed.");
        }

        targetUser.getFriends().remove(friendUser);
        friendUser.getFriends().remove(targetUser);
        userRepository.save(friendUser);
        userRepository.save(targetUser);

        UserDTO userDTO = User.convertToDTO(targetUser);
        userDTO.setFriendsList(convertUserFriendsToDTOList(targetUser.getFriends()));

        return ResponseEntity.ok(userDTO);
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

    private List<UserDTO> convertUserFriendsToDTOList(List<User> friends) {
        List<UserDTO> targetUserFriends = new ArrayList<>();
        for (User friend : friends) {
            UserDTO friendDTO = new UserDTO();
            friendDTO.setId(friend.getId());
            friendDTO.setName(friend.getName());
            friendDTO.setEmail(friend.getEmail());
            targetUserFriends.add(friendDTO);
        }
        return targetUserFriends;
    }

}
