package com.Curio.Controllers;

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

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // Post a new answer
    @PostMapping
    public ResponseEntity<Answer> postAnswer(@Valid @RequestBody PostAnswerDTO request) {
        Answer savedAnswer = answerService.postAnswer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnswer);
    }

    // Edit an answer
    @PutMapping("/{id}")
    public ResponseEntity<Answer> editAnswer(@PathVariable Long id,
                                             @RequestParam Long userId,
                                             @RequestBody String newBody) {
        Answer updatedAnswer = answerService.editAnswer(id, userId, newBody);
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
    public ResponseEntity<Page<Answer>> getAnswersByQuestion(
            @PathVariable Long questionId,
            @PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
            Pageable pageable) {

        Page<Answer> answers = answerService.getAnswersByQuestion(questionId, pageable);
        return ResponseEntity.ok(answers);
    }

    // Mark answer as accepted
    @PutMapping("/{id}/accept")
    public ResponseEntity<Answer> markAsAccepted(@PathVariable Long id,
                                                 @RequestParam Long userId,
                                                 @PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
                                                     Pageable pageable) {
        Answer acceptedAnswer = answerService.markAsAccepted(id, userId, pageable);
        return ResponseEntity.ok(acceptedAnswer);
    }
}