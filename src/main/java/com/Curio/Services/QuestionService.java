package com.Curio.Services;

import com.Curio.DTOs.EditQuestionDTO;
import com.Curio.DTOs.PostQuestionDTO;
import com.Curio.DTOs.QuestionResponse;
import com.Curio.Models.Question;
import com.Curio.Models.Tags;
import com.Curio.Models.User;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.TagsRepository;
import com.Curio.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private UserRepository userRepository;

    // Post a question
    public Question postQuestion(PostQuestionDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        //Create a set of tags:
        Set<Tags> tags = new HashSet<>();
        for(String tagName : request.getTags()){
            // if tag already exits in the DB then don't create it, just add it to the set
            // else create a new tag and add it to the set
            Tags tag = tagsRepository.findByTagName(tagName)
                    .orElse(tagsService.createTag(tagName));

            tags.add(tag);
        }

        Question question = Question.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(user)
                .tags(tags) // make sure PostQuestionDTO has Set<Tags>
                .build();

        return questionRepository.save(question);
    }

    // Edit question
    public Question editQuestion(EditQuestionDTO request) {
        System.out.println(request);
        Question question = questionRepository.findById(request.getQuesId())
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
        List<Question> questions = questionRepository.findAllQuestionsByUserId(userId);

//        List<QuestionResponse> result = new ArrayList<>();
//        for(Question question : questions){
//            QuestionResponse response = QuestionResponse.builder()
//                    .title(question.getTitle())
//                    .body(question.getBody())
//                    .build();
//            result.add(response);
//        }
        return questions;
    }

    public Set<QuestionResponse> getQuestionsByTags(Set<String> tags) {
        Set<Question> questions = questionRepository.findByTagNames(tags);

        return questions.stream()
                .map(q -> QuestionResponse.builder()
                        .title(q.getTitle())
                        .body(q.getBody())
                        .build())
                .collect(Collectors.toSet());
    }


    public List<QuestionResponse> searchQuestions(String keyword) {
        List<Question> questions =  questionRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(keyword, keyword);

        List<QuestionResponse> response = new ArrayList<>();
        for(Question question : questions){
            QuestionResponse ques = QuestionResponse.builder()
                    .title(question.getTitle())
                    .body(question.getBody())
                    .build();
            response.add(ques);
        }
        return response;
    }

    public List<QuestionResponse> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();

        List<QuestionResponse> response = new ArrayList<>();

        for(Question question : questions){
            QuestionResponse q = QuestionResponse.builder()
                    .title(question.getTitle())
                    .body(question.getBody())
                    .build();
            response.add(q);
        }
        return response;
    }

//    public List<Question> getQuestionByUser(Long userId){
//        return questionRepository.findAllQuestionsByUserId(userId);
//    }

}
