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

    // --- User <-> Tag Many-to-Many ---
    @ManyToMany
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tags> tags;

    // --- User <-> Question One-to-Many ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions;

    // --- User <-> Answer One-to-Many ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers;

    // --- Self-referencing Many-to-Many for Followers/Following ---
    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),            // user who follows
            inverseJoinColumns = @JoinColumn(name = "follower_id")  // user being followed
    )
    private Set<User> following;

    @ManyToMany(mappedBy = "following")
    private Set<User> followers;
}

