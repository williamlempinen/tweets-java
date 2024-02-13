package com.example.java_tweets.services;

import com.example.java_tweets.models.User;
import com.example.java_tweets.models.dtos.request.UserCreateDTO;
import com.example.java_tweets.models.dtos.request.UserLoginDTO;
import com.example.java_tweets.models.dtos.response.UserDTO;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO login(UserLoginDTO userLoginDTO) throws Exception {
        User targetUser = userRepository.findByEmail(userLoginDTO.getEmail());

        if (passwordEncoder.matches(CharBuffer.wrap(userLoginDTO.getPassword()), targetUser.getPassword())) {
            UserDTO userDTO = User.convertToDTO(targetUser);
            userDTO.setFriendsList(convertUserFriendsToDTOList(targetUser.getFriends()));
            return userDTO;
        }

        throw new Exception("Error, login");
    }

    public UserDTO findByLogin(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return User.convertToDTO(user);
        }
        throw new Exception("Error, findByLogin");
    }

    public void createUser(UserCreateDTO userCreateDTO) {
        try {
            User newUser = new User();
            newUser.setEmail(userCreateDTO.getEmail());
            newUser.setName(userCreateDTO.getName());
            newUser.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
            userRepository.save(newUser);
        } catch (Exception e) {
            System.err.println("error creating user, service " + e);
        }
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
