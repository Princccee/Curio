package com.Curio.Controllers;

import com.Curio.DTOs.PostAnswerDTO;
import com.Curio.Models.Answer;
import com.Curio.Services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public Answer postAnswer(@RequestBody PostAnswerDTO request) {
        return answerService.postAnswer(request);
    }

    @PutMapping("/{id}")
    public Answer editAnswer(@PathVariable Long id, @RequestParam Long userId,  @RequestBody String newBody) {
        return answerService.editAnswer(id, userId, newBody);
    }

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id, @RequestParam Long userId) {
        answerService.deleteAnswer(id, userId);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Page<Answer>> getAnswersByQuestion(
            @PathVariable Long questionId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Answer> answers = answerService.getAnswersByQuestion(questionId, pageable);
        return ResponseEntity.ok(answers);
    }

    @PutMapping("/{id}/accept")
    public Answer markAsAccepted(
            @PathVariable Long id,
            @RequestParam Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return answerService.markAsAccepted(id, userId, pageable);
    }
}
