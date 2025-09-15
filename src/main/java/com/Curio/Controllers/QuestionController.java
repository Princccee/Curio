package com.Curio.Controllers;

import com.Curio.DTOs.EditQuestionDTO;
import com.Curio.DTOs.PostQuestionDTO;
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
    public ResponseEntity<Question> postQuestion(@Valid @RequestBody PostQuestionDTO request) {
        Question savedQuestion = questionService.postQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }

    // Edit a question
    @PutMapping("/{id}")
    public ResponseEntity<Question> editQuestion(@PathVariable Long id,
                                                 @Valid @RequestBody EditQuestionDTO request) {
        Question updated = questionService.editQuestion(request);
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
    public ResponseEntity<List<Question>> getQuestionsByUser(@PathVariable Long userId) {
        List<Question> questions = questionService.getQuestionByUser(userId);
        return ResponseEntity.ok(questions);
    }

    // Get questions by tags with pagination
    @GetMapping("/tags")
    public ResponseEntity<Page<Question>> getQuestionsByTags(@RequestParam Set<String> tags,
                                                             Pageable pageable) {
        Page<Question> questions = questionService.getQuestionsByTags(tags, pageable);
        return ResponseEntity.ok(questions);
    }

    // Search questions
    @GetMapping("/search")
    public ResponseEntity<Page<Question>> searchQuestions(@RequestParam String keyword,
                                                          Pageable pageable) {
        Page<Question> results = questionService.searchQuestions(keyword, pageable);
        return ResponseEntity.ok(results);
    }
}
