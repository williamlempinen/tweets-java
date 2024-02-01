package com.example.java_tweets.repositorys;

import com.example.java_tweets.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Page<User> findByNameContainingIgnoreCase(String content, Pageable pageable);

}
