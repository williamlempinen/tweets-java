package com.example.java_tweets.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tweet {

    //repostaukset, commentit, timestamp

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    private User tweetOwner;

    private String content;

    @OneToMany(mappedBy = "onTweet")
    @JsonManagedReference
    private List<Comment> tweetComments;

    private Integer likes;

    private LocalDateTime timeStamp;

    public Tweet() {
        this.likes = 0;
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

    public Integer getLikes() { return likes; }

    public void setLikes(Integer likes) { this.likes = likes; }

    public void like() { this.likes = likes + 1; }

    public LocalDateTime getTimeStamp() { return timeStamp; }

    public void setTimeStamp(LocalDateTime localDateTime) { this.timeStamp = localDateTime; }

    public List<Comment> getTweetComments() { return tweetComments; }

    public void addNewComment(Comment comment) {
        tweetComments.add(comment);
    }

}
