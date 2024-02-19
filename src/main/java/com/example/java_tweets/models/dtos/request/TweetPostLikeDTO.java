package com.example.java_tweets.models.dtos.request;

public class TweetPostLikeDTO {
    private int tweetId;
    private int userId;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }
}
