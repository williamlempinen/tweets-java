package com.example.java_tweets.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    //add password
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "tweetOwner")
    @JsonManagedReference
    private List<Tweet> tweetList;

    @OneToMany(mappedBy = "commentOwner")
    @JsonManagedReference
    private List<Comment> commentList;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore
    private List<User> friends;

    public User() {
        this.tweetList = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.commentList = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public void addTweet(Tweet tweet) {
        tweetList.add(tweet);
    }

    public List<User> getFriends() {
        return friends;
    }

    public void addNewFriend(User user) {
        friends.add(user);
    }

    public List<Comment> getCommentList() { return commentList; }

    public void addNewComment(Comment comment) {
        commentList.add(comment);
    }
}
