package com.example.java_tweets.models.dtos.response;

import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.Tweet;

import java.util.List;

public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private List<Tweet> tweetList;
    private List<Comment> commentList;
    private List<UserDTO> friendsList;
    private String token;

    public List<UserDTO> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<UserDTO> friendsList) {
        this.friendsList = friendsList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public void setTweetList(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tweetList=" + tweetList +
                ", commentList=" + commentList +
                ", friendsList=" + friendsList +
                '}';
    }
}
