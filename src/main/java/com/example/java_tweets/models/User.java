package com.example.java_tweets.models;


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

    private String password;

    @OneToMany(mappedBy = "tweetOwner", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tweet> tweetList;

    @OneToMany(mappedBy = "commentOwner", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> commentList;

    private List<String> friends;

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

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public void addTweet(Tweet tweet) {
        tweetList.add(tweet);
    }

    public List<String> getFriends() {
        return friends;
    }

    public void addNewFriend(String userFriend) {
        friends.add(userFriend);
    }

    public List<Comment> getCommentList() { return commentList; }

    public void addNewComment(Comment comment) {
        commentList.add(comment);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", tweetList=" + tweetList +
                ", commentList=" + commentList +
                ", friends=" + friends +
                '}';
    }
}
