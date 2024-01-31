package com.example.java_tweets.models.dtos.request;

public class UserFriendStatusDTO {
    private Integer userId;
    private Integer friendUserId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Integer friendUserId) {
        this.friendUserId = friendUserId;
    }

}
