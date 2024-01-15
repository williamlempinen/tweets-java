package com.example.java_tweets.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    private User commentOwner;

    @ManyToOne
    @JsonBackReference
    private Tweet onTweet;

    private String content;

    private Integer likes;

    private LocalDateTime timeStamp;

    public Comment() {
        this.timeStamp = LocalDateTime.now();
        this.likes = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(User commentOwner) {
        this.commentOwner = commentOwner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimeStamp() { return timeStamp; }

    public void setTimeStamp(LocalDateTime localDateTime) { this.timeStamp = localDateTime; }

    public Integer getLikes() { return likes; }

    public void setLikes(Integer likes) { this.likes = likes; }

    public void like() { this.likes = likes + 1; }

    public Tweet getOnTweet() {
        return onTweet;
    }

    public void setOnTweet(Tweet tweet) {
        this.onTweet = tweet;
    }
}
