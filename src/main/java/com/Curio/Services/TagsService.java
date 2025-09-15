package com.Curio.Services;

import com.Curio.Models.Tags;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService {

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Create new tag (if it doesn’t exist)
    public Tags createTag(String tagName) {
        Tags tag = tagsRepository.findByTagName(tagName);
        if (tag == null) {
            tag = Tags.builder()
                    .tagName(tagName)
                    .build();
        }
        return tag;
    }

    // Get all tags
    public List<Tags> getAllTags() {
        return tagsRepository.findAll();
    }

//    // Get questions by tag
//    public List<Question> getQuestionsByTag(Long tagId) {
//        Tags tag = tagsRepository.findById(tagId)
//                .orElseThrow(() -> new RuntimeException("Tag not found"));
//        return questionRepository.findByTagsIn(Set.of(tag), null).getContent(); // pageable null = all
//    }
}
