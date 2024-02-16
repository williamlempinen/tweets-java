package com.example.java_tweets.services;

import com.example.java_tweets.config.exceptions.AuthException;
import com.example.java_tweets.config.exceptions.DataAccessException;
import com.example.java_tweets.config.exceptions.UserNotFoundException;
import com.example.java_tweets.models.User;
import com.example.java_tweets.models.dtos.request.CommentEventDTO;
import com.example.java_tweets.models.dtos.request.UserCreateDTO;
import com.example.java_tweets.models.dtos.request.UserFriendStatusDTO;
import com.example.java_tweets.models.dtos.request.UserLoginDTO;
import com.example.java_tweets.models.dtos.response.UserDTO;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO login(UserLoginDTO userLoginDTO) throws UserNotFoundException, AuthException {
        User targetUser = userRepository.findByEmail(userLoginDTO.getEmail());

        if (targetUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found with email: " + userLoginDTO.getEmail());
        }
        if (!passwordEncoder.matches(CharBuffer.wrap(userLoginDTO.getPassword()), targetUser.getPassword())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Incorrect credentials");
        }
        UserDTO userDTO = User.convertToDTO(targetUser);
        userDTO.setFriendsList(convertUserFriendsToDTOList(targetUser.getFriends()));
        return userDTO;
    }

    public UserDTO findByLogin(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found with email: " + email);
        }
        return User.convertToDTO(user);
    }

    public void createUser(UserCreateDTO userCreateDTO) {
        try {
            User newUser = new User();
            newUser.setEmail(userCreateDTO.getEmail());
            newUser.setName(userCreateDTO.getName());
            newUser.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<UserDTO> searchUsers(String query, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userRepository.findByNameContainingIgnoreCase(query, pageable);
            return users.map(User::convertToDTO);
        } catch (Exception e) {
            throw new DataAccessException(HttpStatus.BAD_REQUEST, "Error accessing data");
        }
    }

    public List<UserDTO> findAllUserDTOs() {
        try {
            List<User> allUsers = userRepository.findAll();
            return allUsers.stream().map(user -> {
                UserDTO userDTO = User.convertToDTO(user);
                List<UserDTO> userFriendDTOs = convertUserFriendsToDTOList(user.getFriends());
                userDTO.setFriendsList(userFriendDTOs);
                return userDTO;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DataAccessException(HttpStatus.BAD_REQUEST, "Error accessing data");
        }
    }

    public UserDTO addFriend(UserFriendStatusDTO userFriendStatusDTO) throws UserNotFoundException, DataAccessException {
        User targetUser = userRepository.findById(userFriendStatusDTO.getUserId()).orElse(null);
        User friendUser = userRepository.findById(userFriendStatusDTO.getFriendUserId()).orElse(null);

        if (targetUser == null || friendUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (targetUser.getFriends().contains(friendUser)) {
            //not the best exception...
            throw new DataAccessException(HttpStatus.BAD_REQUEST, "Already friends");
        }
        targetUser.getFriends().add(friendUser);
        friendUser.getFriends().add(targetUser);
        userRepository.save(friendUser);
        userRepository.save(targetUser);
        UserDTO userDTO = User.convertToDTO(targetUser);
        userDTO.setFriendsList(convertUserFriendsToDTOList(targetUser.getFriends()));
        return userDTO;
    }

    public UserDTO removeFriend(UserFriendStatusDTO userFriendStatusDTO) throws UserNotFoundException, DataAccessException {
        User targetUser = userRepository.findById(userFriendStatusDTO.getUserId()).orElse(null);
        User friendUser = userRepository.findById(userFriendStatusDTO.getFriendUserId()).orElse(null);

        if (targetUser == null || friendUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (targetUser.getFriends().contains(friendUser)) {
            //not the best exception...
            throw new DataAccessException(HttpStatus.BAD_REQUEST, "User doesn't exist in your friends.");
        }
        targetUser.getFriends().remove(friendUser);
        friendUser.getFriends().remove(targetUser);
        userRepository.save(friendUser);
        userRepository.save(targetUser);
        UserDTO userDTO = User.convertToDTO(targetUser);
        userDTO.setFriendsList(convertUserFriendsToDTOList(targetUser.getFriends()));
        return userDTO;
    }

    public List<UserDTO> getFriends(int userId) throws UserNotFoundException {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        return targetUser.getFriends()
                .stream()
                .map(User::convertToDTO)
                .collect(Collectors.toList());
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
