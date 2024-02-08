package com.example.java_tweets.models.dtos.request;

public class UserFriendStatusDTO {
    private Integer userId;
    private Integer friendUserId;
    private String friendUserName;
    private String friendUserEmail;

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

    public String getFriendUserName() {
        return friendUserName;
    }

    public void setFriendUserName(String friendUserName) {
        this.friendUserName = friendUserName;
    }

    public String getFriendUserEmail() {
        return friendUserEmail;
    }

    public void setFriendUserEmail(String friendUserEmail) {
        this.friendUserEmail = friendUserEmail;
    }

    @Override
    public String toString() {
        return "UserFriendStatusDTO{" +
                "userId=" + userId +
                ", friendUserId=" + friendUserId +
                ", friendUserName='" + friendUserName + '\'' +
                ", friendUserEmail='" + friendUserEmail + '\'' +
                '}';
    }
}
