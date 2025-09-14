package com.Curio.Repositories;

import com.Curio.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // Get all the following of an user
    @Query("SELECT u.following FROM User u WHERE u.id = :userId")
    List<User> findAllFollowings(@Param("userId") Long userId);
}
