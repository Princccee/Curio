package com.Curio.Controllers;

import com.Curio.DTOs.PostCommentDTO;
import com.Curio.Models.Comment;
import com.Curio.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment addComment(@RequestBody PostCommentDTO request) {
        return commentService.addCommentToQuestion(request);
    }

    @PutMapping("/{id}")
    public Comment editComment(@PathVariable Long id, @RequestParam Long userId, @RequestParam String body) {
        return commentService.editComment(id, userId, body);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id, @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
    }

}
