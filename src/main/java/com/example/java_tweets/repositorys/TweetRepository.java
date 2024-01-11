package com.example.java_tweets.repositorys;

import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Integer> {
    Iterable<Tweet> findByOwner(User owner);
}
