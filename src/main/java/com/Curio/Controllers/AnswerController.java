package com.Curio.Controllers;

import com.Curio.DTOs.AnswerResponse;
import com.Curio.DTOs.PostAnswerDTO;
import com.Curio.Models.Answer;
import com.Curio.Services.AnswerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // Post a new answer
    @PostMapping
    public ResponseEntity<AnswerResponse> postAnswer(@Valid @RequestBody PostAnswerDTO request) {
        AnswerResponse savedAnswer = answerService.postAnswer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnswer);
    }

    // Edit an answer
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> editAnswer(@PathVariable Long id,
                                             @RequestParam Long userId,
                                             @RequestParam String newBody) {
        AnswerResponse updatedAnswer = answerService.editAnswer(id, userId, newBody);
        return ResponseEntity.ok(updatedAnswer);
    }

    // Delete an answer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id,
                                             @RequestParam Long userId) {
        answerService.deleteAnswer(id, userId);
        return ResponseEntity.noContent().build(); // returns 204
    }

    // Get answers for a question (paginated, newest first)
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByQuestion(
            @PathVariable Long questionId) {

        List<AnswerResponse> answers = answerService.getAnswersByQuestion(questionId);
        return ResponseEntity.ok(answers);
    }

    // Mark answer as accepted
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> markAsAccepted(@PathVariable Long id,
                                                 @RequestParam Long userId) {
        Answer acceptedAnswer = answerService.markAsAccepted(id, userId);
        return ResponseEntity.ok("Successfully accepted");
    }
}