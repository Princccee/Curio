package com.Curio.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote extends BaseModel {
    private boolean upvote; // true = upvote, false = downvote

    // Many votes can be polled by a single user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // A question as well can have many votes
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // An answer can also have many votes
    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
