package com.Curio.Repositories;

import com.Curio.Models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Integer countByQuestionIdAndUpvote(Long quesId, Boolean upVote);

    Integer countByAnswerIdAndUpvote(Long ansId, Boolean upVote);
}
