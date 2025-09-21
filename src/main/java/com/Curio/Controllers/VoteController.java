package com.Curio.Controllers;

import com.Curio.DTOs.VoteDTO;
import com.Curio.Models.Vote;
import com.Curio.Services.VoteService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/question")
    public ResponseEntity<?> voteQuestion(@RequestBody VoteDTO request){
         voteService.voteQuestion(request);
         return ResponseEntity.ok("Successful");
    }

    @PostMapping("/answer")
    public ResponseEntity<?> voteAnswer(@RequestBody VoteDTO request){
        voteService.voteAnswer(request);
        return ResponseEntity.ok("Successful");
    }

    @GetMapping("/count_ques/{quesId}/{upvote}")
    public long countVotesQues(@PathVariable Long quesId, @PathVariable boolean upvote){
        return  voteService.countVotesForQuestion(quesId, upvote);
    }

    @GetMapping("/count_ans/{ansId}/{upvote}")
    public long countVotesAns(@PathVariable Long ansId, @PathVariable boolean upvote){
        return  voteService.countVotesForAnswer(ansId, upvote);
    }
}
