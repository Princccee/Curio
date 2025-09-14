package com.Curio.Controllers;

import com.Curio.DTOs.EditQuestionDTO;
import com.Curio.DTOs.PostQuestionDTO;
import com.Curio.Models.Question;
import com.Curio.Services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public Question postQuestion(@RequestBody PostQuestionDTO request) {
        return questionService.postQuestion(request);
    }

    @PutMapping("/{id}")
    public Question editQuestion(@PathVariable Long id, @RequestBody EditQuestionDTO request) {
        return questionService.editQuestion(request);
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id, @RequestParam Long userId) {
        questionService.deleteQuestion(id, userId);
        return "Question deleted successfully";
    }

    @GetMapping("/user/{userId}")
    public List<Question> getQuestionsByUser(@PathVariable Long userId) {
        return questionService.getQuestionByUser(userId);
    }

    @GetMapping("/tags")
    public Page<Question> getQuestionsByTags(@RequestParam Set<String> tags, Pageable pageable) {
        return questionService.getQuestionsByTags(tags, pageable);
    }

    @GetMapping("/search")
    public Page<Question> searchQuestions(@RequestParam String keyword, Pageable pageable) {
        return questionService.searchQuestions(keyword, pageable);
    }
}
