package com.example.java_tweets.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    private User tweetOwner;

    private Integer ownerId;

    private String ownerEmail;

    private String content;

    private String title;

    @OneToMany(mappedBy = "onTweet")
    @JsonManagedReference
    private List<Comment> tweetComments;

    @ElementCollection
    private Set<Integer> likes;

    private String ownerName;

    private LocalDateTime timeStamp;

    public Tweet() {
        this.likes = new HashSet<>();
        this.timeStamp = LocalDateTime.now();
        this.tweetComments = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getTweetOwner() {
        return tweetOwner;
    }

    public void setTweetOwner(User owner) {
        this.tweetOwner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikesCount() {
        return likes.size();
    }

    public void setLike(Integer userId) {
        if (!likes.remove(userId)) {
            likes.add(userId);
        }
    }

    public Set<Integer> getLikes() {
        return likes;
    }

    public LocalDateTime getTimeStamp() { return timeStamp; }

    public void setTimeStamp(LocalDateTime localDateTime) { this.timeStamp = localDateTime; }

    public List<Comment> getTweetComments() { return tweetComments; }

    public void addNewComment(Comment comment) {
        tweetComments.add(comment);
    }

    public String getOwnerName() { return ownerName; }

    public void setOwnerName(String name) { this.ownerName = name; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
