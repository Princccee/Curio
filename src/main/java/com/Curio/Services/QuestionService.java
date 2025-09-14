package com.Curio.Services;

import com.Curio.DTOs.EditQuestionDTO;
import com.Curio.DTOs.PostQuestionDTO;
import com.Curio.Models.Question;
import com.Curio.Models.Tags;
import com.Curio.Models.User;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    // Post a question
    public Question postQuestion(PostQuestionDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        Question question = Question.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(user)
                .tags(request.getTags()) // make sure PostQuestionDTO has Set<Tags>
                .build();

        return questionRepository.save(question);
    }

    // Edit question
    public Question editQuestion(EditQuestionDTO request) {
        Question question = questionRepository.findById(request.getQid())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Unauthorized: You can only edit your own questions");
        }

        question.setTitle(request.getTitle());
        question.setBody(request.getBody());
        return questionRepository.save(question);
    }

    // Delete Question
    public void deleteQuestion(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own questions");
        }

        questionRepository.delete(question);
    }

    public List<Question> getQuestionsByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        return questionRepository.findAllQuestionsByUserId(userId);
    }

    public Page<Question> getQuestionsByTags(Set<Tags> tags, Pageable pageable) {
        return questionRepository.findByTagsIn(tags, pageable);
    }

    public Page<Question> searchQuestions(String keyword, Pageable pageable) {
        return questionRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(keyword, keyword, pageable);
    }

    public Page<Question> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

}
