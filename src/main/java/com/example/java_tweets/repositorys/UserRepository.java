package com.example.java_tweets.repositorys;

import com.example.java_tweets.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

}
