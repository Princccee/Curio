package com.Curio.Services;

import com.Curio.DTOs.VoteDTO;
import com.Curio.Models.Answer;
import com.Curio.Models.Question;
import com.Curio.Models.User;
import com.Curio.Models.Vote;
import com.Curio.Repositories.AnswerRepository;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.UserRepository;
import com.Curio.Repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    // Upvote/Downvote a question
    public boolean voteQuestion(VoteDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Question question = questionRepository.findById(request.getQuesId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // Check if the user already voted on this question
        Optional<Vote> existingVoteOpt = voteRepository.findByUserAndQuestion(user, question);

        if (existingVoteOpt.isPresent()) {
            Vote existingVote = existingVoteOpt.get();

            // Toggle the vote (upvote -> downvote, downvote -> upvote)
            existingVote.setUpvote(!existingVote.isUpvote());
            voteRepository.save(existingVote);
        } else {
            // No vote exists → create new one
            Vote vote = Vote.builder()
                    .user(user)
                    .question(question)
                    .upvote(true) // default as upvote on first click
                    .build();
            voteRepository.save(vote);
        }

        return true;
    }

    // Upvote/Downvote an answer
    public boolean voteAnswer(VoteDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Answer answer = answerRepository.findById(request.getAnsId())
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        // Check if the user already voted on this question
        Optional<Vote> existingVoteOpt = voteRepository.findByUserAndAnswer(user, answer);

        if (existingVoteOpt.isPresent()) {
            Vote existingVote = existingVoteOpt.get();

            // Toggle the vote (upvote -> downvote, downvote -> upvote)
            existingVote.setUpvote(!existingVote.isUpvote());
            voteRepository.save(existingVote);
        } else {
            // No vote exists → create new one
            Vote vote = Vote.builder()
                    .user(user)
                    .answer(answer)
                    .upvote(request.isVote())
                    .build();
            voteRepository.save(vote);
        }
        return true;
    }

    // Count votes for a question
    public long countVotesForQuestion(Long questionId, boolean upvote) {
        return voteRepository.countByQuestionIdAndUpvote(questionId, upvote);
    }

    // Count votes for an answer
    public long countVotesForAnswer(Long answerId, boolean upvote) {
        return voteRepository.countByAnswerIdAndUpvote(answerId, upvote);
    }
}
