package com.Curio.Services;

import com.Curio.Configurations.JwtUtil;
import com.Curio.DTOs.LoginDTO;
import com.Curio.DTOs.RegisterDTO;
import com.Curio.DTOs.UpdateProfileDTO;
import com.Curio.Models.User;
import com.Curio.Repositories.TagsRepository;
import com.Curio.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public User createUser(RegisterDTO request){
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("User already exists");

        //Create a User entity:
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .bio(request.getBio())
                .build();

        return userRepository.save(user);
    }

    public ResponseEntity<?> authenticateUser(LoginDTO request){
        //Find the user:
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User doesn't exist"));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return ResponseEntity.ok("Login Successful");
        else return ResponseEntity.ok("Invalid Password");
    }

    public Optional<User> getUserProfile(Long userId){
        return userRepository.findById(userId);
    }

    public User updateProfile(UpdateProfileDTO requets){
        User user = userRepository.findById(requets.getUserId())
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        // Update the details.
        user.setName(requets.getName());
        user.setBio(requets.getBio());
        user.setEmail(requets.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    public boolean followOrUnfollow(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User 1 not found"));

        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        // prevent self-follow
        if (user1.equals(user2)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }

        // if already following -> unfollow
        if (user1.getFollowing().contains(user2)) {
            user1.getFollowing().remove(user2);
        } else {
            // else -> follow
            user1.getFollowing().add(user2);
        }

        userRepository.save(user1); // persist changes
        return true;
    }

}
