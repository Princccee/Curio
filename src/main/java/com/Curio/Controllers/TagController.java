package com.Curio.Controllers;

import java.util.*;
import com.Curio.Models.Tags;
import com.Curio.Services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagsService tagsService;

    @PostMapping("/create/{tagName}")
    public ResponseEntity<Tags> createTag(@RequestParam String tagName){
        return ResponseEntity.status(HttpStatus.CREATED).body(tagsService.createTag(tagName));
    }

    @GetMapping()
    public ResponseEntity<List<Tags>> getAllTags(){
        return ResponseEntity.ok(tagsService.getAllTags());
    }
}
