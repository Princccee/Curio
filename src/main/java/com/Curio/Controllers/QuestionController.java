package com.Curio.Controllers;

import com.Curio.DTOs.EditQuestionDTO;
import com.Curio.DTOs.PostQuestionDTO;
import com.Curio.DTOs.QuestionResponse;
import com.Curio.Models.Question;
import com.Curio.Services.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Post a new question
    @PostMapping
    public ResponseEntity<QuestionResponse> postQuestion(@RequestBody PostQuestionDTO request) {
        QuestionResponse savedQuestion = questionService.postQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }

    // Edit a question
    @PutMapping("/edit")
    public ResponseEntity<EditQuestionDTO> editQuestion(@RequestBody EditQuestionDTO request) {
        EditQuestionDTO updated = questionService.editQuestion(request);
        return ResponseEntity.ok(updated);
    }

    // Delete a question
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id,
                                               @RequestParam Long userId) {
        questionService.deleteQuestion(id, userId);
        return ResponseEntity.noContent().build(); // returns 204
    }

    // Get questions by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByUser(@PathVariable Long userId) {
        List<QuestionResponse> questions = questionService.getQuestionsByUser(userId);
        return ResponseEntity.ok(questions);
    }

    // Get questions by tags with pagination
    @GetMapping("/tags")
    public ResponseEntity<Set<QuestionResponse>> getQuestionsByTags(@RequestParam Set<String> tags) {
        Set<QuestionResponse> questions = questionService.getQuestionsByTags(tags);
        return ResponseEntity.ok(questions);
    }

    // Search questions
    @GetMapping("/search")
    public ResponseEntity<List<QuestionResponse>> searchQuestions(@RequestParam String keyword) {
        List<QuestionResponse> results = questionService.searchQuestions(keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAllQuestions(){
        List<QuestionResponse> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
}
