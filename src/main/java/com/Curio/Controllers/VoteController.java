package com.Curio.Controllers;

import com.Curio.DTOs.VoteDTO;
import com.Curio.Models.Vote;
import com.Curio.Services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/question")
    public Vote voteQuestion(@RequestBody VoteDTO request){
        return voteService.voteQuestion(request);
    }

    @PostMapping("/answer")
    public Vote voteAnswer(@RequestBody VoteDTO request){
        return voteService.voteAnswer(request);
    }

    @GetMapping("/count_ques")
    public long countVotesQues(@PathVariable Long quesId, @PathVariable boolean upvote){
        return  voteService.countVotesForQuestion(quesId, upvote);
    }

    @GetMapping("/count_ans")
    public long countVotesAns(@PathVariable Long ansId, @PathVariable boolean upvote){
        return  voteService.countVotesForAnswer(ansId, upvote);
    }
}
