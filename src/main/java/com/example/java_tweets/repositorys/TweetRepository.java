package com.example.java_tweets.repositorys;

import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    List<Tweet> findByTweetOwner(User tweetOwner);

    Page<Tweet> findByContentContainingIgnoreCase(String content, Pageable pageable);
}
