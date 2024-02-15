package com.example.java_tweets.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JsonBackReference
    private User commentOwner;

    @ManyToOne
    @JsonBackReference
    private Tweet onTweet;

    private String content;

    private String ownerName;

    @ElementCollection
    private Set<Integer> likes;

    private LocalDateTime timeStamp;

    public Comment() {
        this.timeStamp = LocalDateTime.now();
        this.likes = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getOwnerName() { return ownerName; }

    public void setOwnerName(String name) { this.ownerName = name; }

    public LocalDateTime getTimeStamp() { return timeStamp; }

    public void setTimeStamp(LocalDateTime localDateTime) { this.timeStamp = localDateTime; }

    public int getLikesCount() {
        return likes.size();
    }

    public void setLike(int userId) {
        if (!likes.remove(userId)) {
            likes.add(userId);
        }
    }

    public Set<Integer> getLikes() {
        return likes;
    }

    public Tweet getOnTweet() {
        return onTweet;
    }

    public void setOnTweet(Tweet tweet) {
        this.onTweet = tweet;
    }
}
