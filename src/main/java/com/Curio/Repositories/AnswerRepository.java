package com.Curio.Repositories;

import com.Curio.Models.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // We want to have limited number of answer to be showed for a question and that can be achieved by pagination mechanism. Now using Page every question will have a limited set of answers at once.
    List<Answer> findByQuestionId(Long questionId);

}
