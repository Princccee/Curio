package com.Curio.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseModel {
    @Column(columnDefinition = "TEXT")
    private String body;

    // Many answer can belong to one question
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // Many answers can be posted by one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
