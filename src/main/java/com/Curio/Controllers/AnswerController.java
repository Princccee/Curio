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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // Post a new answer
    @PostMapping
    public ResponseEntity<AnswerResponse> postAnswer(@Valid @RequestBody PostAnswerDTO request) {
        Answer savedAnswer = answerService.postAnswer(request);

        AnswerResponse response =  AnswerResponse.builder()
                .quesId(savedAnswer.getQuestion().getId())
                .userId(savedAnswer.getUser().getId())
                .title(savedAnswer.getQuestion().getTitle())
                .body(savedAnswer.getQuestion().getBody())
                .ans(savedAnswer.getBody())
                .build();

        return ResponseEntity.ok(response);
    }

    // Edit an answer
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> editAnswer(@PathVariable Long id,
                                             @RequestParam Long userId,
                                             @RequestParam String newBody) {
        Answer updatedAnswer = answerService.editAnswer(id, userId, newBody);

        AnswerResponse response =  AnswerResponse.builder()
                .quesId(updatedAnswer.getQuestion().getId())
                .userId(updatedAnswer.getUser().getId())
                .title(updatedAnswer.getQuestion().getTitle())
                .body(updatedAnswer.getQuestion().getBody())
                .ans(updatedAnswer.getBody())
                .build();

        return ResponseEntity.ok(response);
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

        List<Answer> answers = answerService.getAnswersByQuestion(questionId);

        List<AnswerResponse> answerResponses = new ArrayList<>();
        for(Answer answer : answers){
            AnswerResponse ans = AnswerResponse.builder()
                    .quesId(answer.getQuestion().getId())
                    .title(answer.getQuestion().getTitle())
                    .body(answer.getQuestion().getBody())
                    .ans(answer.getBody())
                    .build();
            answerResponses.add(ans);
        }

        return ResponseEntity.ok(answerResponses);
    }

    // Mark answer as accepted
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> markAsAccepted(@PathVariable Long id,
                                                 @RequestParam Long userId) {
        Answer acceptedAnswer = answerService.markAsAccepted(id, userId);
        return ResponseEntity.ok("Successfully accepted");
    }
}