package com.example.java_tweets.models;


import com.example.java_tweets.models.dtos.response.UserDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    private String password;

    @OneToMany(mappedBy = "tweetOwner", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Tweet> tweetList;

    @OneToMany(mappedBy = "commentOwner", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Comment> commentList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;

    public User() {
        this.tweetList = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.commentList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<User> getFriends() {
        return friends;
    }

    public void addNewFriend(User userFriend) {
        friends.add(userFriend);
    }

    public List<Comment> getCommentList() { return commentList; }

    public void addNewComment(Comment comment) {
        commentList.add(comment);
    }

    public static UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTweetList(user.getTweetList());
        dto.setCommentList(user.getCommentList());

        return dto;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
