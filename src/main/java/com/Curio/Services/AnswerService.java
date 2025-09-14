package com.Curio.Services;

import com.Curio.DTOs.PostAnswerDTO;
import com.Curio.Models.Answer;
import com.Curio.Models.Question;
import com.Curio.Models.User;
import com.Curio.Repositories.AnswerRepository;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Answer postAnswer(PostAnswerDTO request) {
        Question question = questionRepository.findById(request.getQuesId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create an answer to a question
        Answer answer = Answer.builder()
                .body(request.getAnswer())
                .question(question)
                .user(user)
                .isAccepted(false) // by default make it false
                .build();

        return answerRepository.save(answer);
    }

    public Answer editAnswer(Long answerId, Long userId, String newBody) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        if (!answer.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only edit your own answers");
        }

        answer.setBody(newBody);
        return answerRepository.save(answer);
    }

    public void deleteAnswer(Long answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        if (!answer.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own answers");
        }

        answerRepository.delete(answer);
    }

    public Page<Answer> getAnswersByQuestion(Long questionId, Pageable page) {
        questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        return answerRepository.findByQuestionId(questionId, page);
    }

    public Answer markAsAccepted(Long answerId, Long userId, Pageable page) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        Question question = answer.getQuestion();

        if (!question.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Only question owner can accept an answer");
        }

        // Unmark previously accepted answer if exists
        Page<Answer> answers = answerRepository.findByQuestionId(question.getId(), page);
        for (Answer a : answers) {
            if (a.isAccepted()) {
                a.setAccepted(false);
                answerRepository.save(a);
            }
        }

        answer.setAccepted(true);
        return answerRepository.save(answer);
    }
}
