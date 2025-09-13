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
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    private String bio;

    // Every user can follow many tags and many tags can be followed by many user.
    // So we gonna have many to many relationship and we will have a common join table with with the user id and tag id on which they will be joined
    @ManyToMany()
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"), // user if from user table
            inverseJoinColumns = @JoinColumn(name = "tag_id") // tag id from tag table
    )
    private Set<Tags> tags;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers;
}
