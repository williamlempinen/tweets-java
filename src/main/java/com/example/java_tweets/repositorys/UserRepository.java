package com.example.java_tweets.repositorys;

import com.example.java_tweets.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
