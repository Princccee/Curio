package com.Curio.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseModel {
    @Column(columnDefinition = "TEXT")
    private String text;

    // Many comments can be made by one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Comment can belong to either a Question or an Answer
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    // Comment can also have replies
    @OneToMany
    @JoinColumn(name = "parent_comment_id")
    private Set<Comment> replies;
}
