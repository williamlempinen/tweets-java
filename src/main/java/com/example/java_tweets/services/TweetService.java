package com.example.java_tweets.services;

import com.example.java_tweets.config.exceptions.DataAccessException;
import com.example.java_tweets.config.exceptions.TweetNotFoundException;
import com.example.java_tweets.config.exceptions.UserNotFoundException;
import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.Tweet;
import com.example.java_tweets.models.User;
import com.example.java_tweets.models.dtos.request.TweetPostCommentDTO;
import com.example.java_tweets.models.dtos.request.TweetPostLikeDTO;
import com.example.java_tweets.models.dtos.request.TweetPostTweetDTO;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.repositorys.TweetRepository;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public TweetService(TweetRepository tweetRepository,
                        UserRepository userRepository,
                        CommentRepository commentRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public void postTweet(TweetPostTweetDTO tweetPostTweetDTO) throws UserNotFoundException {
        User targetUser = userRepository.findById(tweetPostTweetDTO.getUserId()).orElse(null);

        if (targetUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        try {
            Tweet newTweet = new Tweet();
            newTweet.setTweetOwner(targetUser);
            newTweet.setTitle(tweetPostTweetDTO.getTitle());
            newTweet.setContent(tweetPostTweetDTO.getContent());
            newTweet.setOwnerName(targetUser.getName());
            newTweet.setOwnerId(targetUser.getId());
            newTweet.setOwnerEmail(tweetPostTweetDTO.getUserEmail());
            tweetRepository.save(newTweet);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<Tweet> searchTweets(String query, int page, int size) throws DataAccessException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return tweetRepository.findByContentContainingIgnoreCase(query, pageable);
        } catch (Exception e) {
            throw new DataAccessException(HttpStatus.BAD_REQUEST, "Error accessing data");
        }
    }

    public Page<Tweet> findAllTweets(int page, int size) throws DataAccessException {
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, "timeStamp");
            Pageable pageable = PageRequest.of(page, size, sort);
            return tweetRepository.findAll(pageable);
        } catch (Exception e) {
            throw new DataAccessException(HttpStatus.BAD_REQUEST, "Error accessing data");
        }
    }

    public List<Tweet> findTweetsByUser(int userId) throws UserNotFoundException {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        return tweetRepository.findByTweetOwner(targetUser);
    }

    public List<Tweet> findByFriendsTweets(int userId) throws UserNotFoundException {
        User targetUser = userRepository.findById(userId).orElse(null);

        if (targetUser == null) {
            throw new UserNotFoundException(HttpStatus.BAD_REQUEST, "User not found");
        }
        //return targetUser's friend's tweets
        return targetUser.getFriends()
                .stream()
                .map(User::getId)
                .flatMap(uId -> findTweetsByUser(uId).stream())
                .collect(Collectors.toList());
    }

    public void likeTweet(TweetPostLikeDTO tweetPostLikeDTO) throws UserNotFoundException, TweetNotFoundException {
        Tweet targetTweet = tweetRepository.findById(tweetPostLikeDTO.getTweetId()).orElse(null);
        User targetUser = userRepository.findById(tweetPostLikeDTO.getUserId()).orElse(null);

        if (targetTweet == null) {
            throw new TweetNotFoundException(HttpStatus.NOT_FOUND, "Tweet not found");
        }
        if (targetUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        targetTweet.setLike(tweetPostLikeDTO.getUserId());
        tweetRepository.save(targetTweet);
    }

    public void deleteTweet(int tweetId) throws TweetNotFoundException {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            throw new TweetNotFoundException(HttpStatus.NOT_FOUND, "Tweet not found");
        }
        commentRepository.deleteByOnTweetId(tweetId);
        tweetRepository.delete(targetTweet);
    }

    public List<Comment> getTweetComment(int tweetId) {
        Tweet targetTweet = tweetRepository.findById(tweetId).orElse(null);

        if (targetTweet == null) {
            throw new TweetNotFoundException(HttpStatus.NOT_FOUND, "Tweet not found");
        }
        return targetTweet.getTweetComments();
    }

    public void addComment(TweetPostCommentDTO tweetPostCommentDTO) throws UserNotFoundException, TweetNotFoundException {
        Tweet targetTweet = tweetRepository.findById(tweetPostCommentDTO.getTweetId()).orElse(null);
        User commenterUser = userRepository.findById(tweetPostCommentDTO.getUserId()).orElse(null);

        if (targetTweet == null) {
            throw new TweetNotFoundException(HttpStatus.NOT_FOUND, "Tweet not found");
        }
        if (commenterUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        try {
            Comment comment = new Comment();
            comment.setCommentOwner(commenterUser);
            comment.setContent(tweetPostCommentDTO.getContent());
            comment.setOnTweet(targetTweet);
            comment.setOwnerName(commenterUser.getName());

            commentRepository.save(comment);
            targetTweet.addNewComment(comment);
            tweetRepository.save(targetTweet);
        } catch (Exception e) {
            throw new DataAccessException(HttpStatus.BAD_REQUEST, "Error creating new comment");
        }
    }

}
