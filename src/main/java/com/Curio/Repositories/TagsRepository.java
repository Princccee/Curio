package com.Curio.Repositories;

import com.Curio.Models.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
}
