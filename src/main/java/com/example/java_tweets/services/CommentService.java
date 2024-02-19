package com.example.java_tweets.services;

import com.example.java_tweets.config.exceptions.CommentNotFoundException;
import com.example.java_tweets.config.exceptions.UserNotFoundException;
import com.example.java_tweets.models.Comment;
import com.example.java_tweets.models.User;
import com.example.java_tweets.models.dtos.request.CommentEventDTO;
import com.example.java_tweets.repositorys.CommentRepository;
import com.example.java_tweets.repositorys.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public void likeComment(CommentEventDTO commentEventDTO) throws CommentNotFoundException {
        Comment targetComment = commentRepository.findById(commentEventDTO.getCommentId()).orElse(null);
        User targetUser = userRepository.findById(commentEventDTO.getUserId()).orElse(null);

        if (targetComment == null) {
            throw new CommentNotFoundException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        if (targetUser == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found");
        }
        targetComment.setLike(targetUser.getId());
        commentRepository.save(targetComment);
    }

    public void deleteComment(CommentEventDTO commentEventDTO) throws CommentNotFoundException {
        Comment targetComment = commentRepository.findById(commentEventDTO.getCommentId()).orElse(null);

        if (targetComment == null) {
            throw new CommentNotFoundException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        if (Objects.equals(targetComment.getCommentOwner().getId(), commentEventDTO.getUserId())
                || Objects.equals(targetComment.getOnTweet().getTweetOwner().getId(), commentEventDTO.getUserId())) {
            commentRepository.delete(targetComment);
        }
    }
}
