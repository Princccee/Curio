package com.Curio.Services;

import com.Curio.Models.Answer;
import com.Curio.Models.Comment;
import com.Curio.Models.Question;
import com.Curio.Models.User;
import com.Curio.Repositories.AnswerRepository;
import com.Curio.Repositories.CommentRepository;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    // Add comment to question
    public Comment addCommentToQuestion(Long userId, Long questionId, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Comment comment = Comment.builder()
                .user(user)
                .question(question)
                .text(text)
                .build();

        return commentRepository.save(comment);
    }

    // Add comment to answer
    public Comment addCommentToAnswer(Long userId, Long answerId, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        Comment comment = Comment.builder()
                .user(user)
                .answer(answer)
                .text(text)
                .build();

        return commentRepository.save(comment);
    }

    // Edit comment
    public Comment editComment(Long commentId, Long userId, String newText) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only edit your own comments");
        }

        comment.setText(newText);
        return commentRepository.save(comment);
    }

    // Delete comment
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    // Get all comments for a question
    public List<Comment> getCommentsByQuestion(Long questionId) {
        return commentRepository.findByQuestionId(questionId);
    }

    // Get all comments for an answer
    public List<Comment> getCommentsByAnswer(Long answerId) {
        return commentRepository.findByAnswerId(answerId);
    }
}
