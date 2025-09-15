package com.Curio.Controllers;

import java.util.*;
import com.Curio.Models.Tags;
import com.Curio.Services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagsService tagsService;

    @PostMapping("/create/{tagName}")
    public Tags createTag(@RequestParam String tagName){
        return tagsService.createTag(tagName);
    }

    @GetMapping()
    public List<Tags> getAllTags(){
        return tagsService.getAllTags();
    }
}
