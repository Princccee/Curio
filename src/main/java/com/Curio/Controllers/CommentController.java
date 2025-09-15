package com.Curio.Controllers;

import com.Curio.DTOs.PostCommentDTO;
import com.Curio.Models.Comment;
import com.Curio.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody PostCommentDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addCommentToQuestion(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> editComment(@PathVariable Long id, @RequestParam Long userId, @RequestParam String body) {
        return ResponseEntity.ok(commentService.editComment(id, userId, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
