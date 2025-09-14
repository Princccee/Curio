package com.Curio.Repositories;

import com.Curio.Models.Question;
import com.Curio.Models.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByTagsIn(Set<Tags> tags, Pageable pageable);

    List<Question> findAllQuestionsByUserId(Long userId);

    // search by keyword
    Page<Question> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String title, String body, Pageable pageable);
}
