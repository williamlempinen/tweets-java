package com.example.java_tweets.repositorys;

import com.example.java_tweets.models.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

}
