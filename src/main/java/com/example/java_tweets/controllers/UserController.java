package com.example.java_tweets.controllers;

import com.example.java_tweets.config.UserAuthProvider;
import com.example.java_tweets.models.*;
import com.example.java_tweets.models.dtos.request.UserCreateDTO;
import com.example.java_tweets.models.dtos.request.UserFriendStatusDTO;
import com.example.java_tweets.models.dtos.request.UserLoginDTO;
import com.example.java_tweets.models.dtos.response.UserDTO;
import com.example.java_tweets.repositorys.UserRepository;
import com.example.java_tweets.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    public UserController(UserRepository userRepository,
                          UserService userService,
                          UserAuthProvider userAuthProvider) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userAuthProvider = userAuthProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO user) {
        UserDTO userDTO = userService.login(user);
        userDTO.setToken(userAuthProvider.createToken(userDTO));
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/create-user")
    public ResponseEntity<Object> createNewUser(@RequestBody UserCreateDTO user) {
        userService.createUser(user);
        return ResponseEntity.ok("User created.");
    }

    @GetMapping("/search")
    public @ResponseBody Iterable<UserDTO> searchUsers(@RequestParam String query,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        return userService.searchUsers(query, page, size);
    }

    //this endpoint is only for testing purposes!!
    @GetMapping("/find-all-info")
    public @ResponseBody List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/find-all")
    public @ResponseBody List<UserDTO> findAllUserDTOs() {
        return userService.findAllUserDTOs();
    }


    //endpoint is to find all users that are friends for some user, no impl
    @GetMapping("/find-all-friends")
    public @ResponseBody List<UserDTO> findAllUsersFriends() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(User::getFriends)
                .flatMap(List::stream)
                .map(User::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping()
    public ResponseEntity<Object> addFriend(@RequestBody UserFriendStatusDTO userFriendStatusDTO) {
        return ResponseEntity.ok(userService.addFriend(userFriendStatusDTO));
    }

    @DeleteMapping()
    public ResponseEntity<Object> removeFriend(@RequestBody UserFriendStatusDTO userFriendStatusDTO) {
        return ResponseEntity.ok(userService.removeFriend(userFriendStatusDTO));
    }

    @GetMapping("/friends")
    public @ResponseBody List<UserDTO> getFriends(@RequestParam int userId) {
        return userService.getFriends(userId);
    }

    @DeleteMapping("/delete-all")
    public @ResponseBody String deleteAll() {
        Iterable<User> userList = userRepository.findAll();
        userRepository.deleteAll(userList);

        return "All users deleted";
    }
}
