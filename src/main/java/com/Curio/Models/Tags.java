package com.Curio.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tags extends BaseModel{
    private String tagName;

    // Every tag can be followed by many users, so we going to have many to many relationship
    @ManyToMany(mappedBy = "tags")
    private Set<User> followers;

    // Every tag can belong to multiple questions as well
    @ManyToMany(mappedBy = "tags")
    private Set<Question> questions;
}
