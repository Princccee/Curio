package com.Curio.Repositories;

import com.Curio.Models.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;


@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
    Optional<Tags> findByTagName(String tagName);
}
