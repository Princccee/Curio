package com.Curio.Controllers;

import com.Curio.Configurations.JwtUtil;
import com.Curio.Configurations.UserPrincipal;
import com.Curio.DTOs.LoginDTO;
import com.Curio.DTOs.RegisterDTO;
import com.Curio.DTOs.UpdateProfileDTO;
import com.Curio.DTOs.UserResponseDTO;
import com.Curio.Models.User;
import com.Curio.Repositories.UserRepository;
import com.Curio.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<User> register(@RequestBody RegisterDTO request){
//        return  ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already registered"));
        }
        User u = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .bio(dto.getBio())
                .build();
        userRepository.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created"));
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDTO request){
//        return userService.authenticateUser(request);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        final String token = jwtUtil.generateToken(principal);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/profile/{userId}")
    public Optional<User> getUserProfile(@PathVariable Long userId){
        return userService.getUserProfile(userId);
    }

    @PutMapping("/editProfile")
    public ResponseEntity<UserResponseDTO> update(@RequestBody UpdateProfileDTO request){
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PostMapping("/{user1}/follow_unfollow/{user2}")
    public boolean followOrUnfollow(@PathVariable Long user1, @PathVariable Long user2){
        return userService.followOrUnfollow(user1, user2);
    }
}
