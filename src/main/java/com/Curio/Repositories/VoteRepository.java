package com.Curio.Repositories;

import com.Curio.Models.Answer;
import com.Curio.Models.Question;
import com.Curio.Models.User;
import com.Curio.Models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Integer countByQuestionIdAndUpvote(Long quesId, Boolean upVote);

    Integer countByAnswerIdAndUpvote(Long ansId, Boolean upVote);

    Optional<Vote> findByUserAndQuestion(User user, Question question);
    Optional<Vote> findByUserAndAnswer(User user, Answer answer);
}
