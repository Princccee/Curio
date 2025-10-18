package com.Curio.Services;

import com.Curio.Configurations.JwtUtil;
import com.Curio.DTOs.LoginDTO;
import com.Curio.DTOs.RegisterDTO;
import com.Curio.DTOs.UpdateProfileDTO;
import com.Curio.DTOs.UserResponseDTO;
import com.Curio.Models.User;
import com.Curio.Repositories.TagsRepository;
import com.Curio.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagsRepository tagsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("encodedPassword")
                .bio("Hello there!")
                .questions(new HashSet<>())
                .answers(new HashSet<>())
                .followers(new HashSet<>())
                .following(new HashSet<>())
                .build();
    }

    // ---------- createUser ----------
    @Test
    void createUser_success() {
        RegisterDTO dto = RegisterDTO.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("password123")
                .bio("This is my bio")
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.createUser(dto);

        assertNotNull(created);
        assertEquals("Alice", created.getName());
        assertEquals("alice@example.com", created.getEmail());
        assertEquals("encodedPassword", created.getPassword());
    }

    @Test
    void createUser_alreadyExists() {
        RegisterDTO dto = RegisterDTO.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("alice@123")
                .bio("This is my bio")
                .build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.createUser(dto));
    }

    // ---------- authenticateUser ----------
    @Test
    void authenticateUser_success() {
        LoginDTO dto = LoginDTO.builder()
                .email("alice@example.com")
                .password("rawPasswer")
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPasswer", "encodedPassword")).thenReturn(true);

        ResponseEntity<?> response = userService.authenticateUser(dto);

        assertEquals("Login Successful", response.getBody());
    }

    @Test
    void authenticateUser_invalidPassword() {
        LoginDTO dto = LoginDTO.builder()
                .email("alice@example.com")
                .password("rawPasswer")
                .build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        ResponseEntity<?> response = userService.authenticateUser(dto);

        assertEquals("Invalid Password", response.getBody());
    }

    @Test
    void authenticateUser_userNotFound() {
        LoginDTO dto = LoginDTO.builder()
                .email("alice@example.com")
                .password("rawPasswer")
                .build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.authenticateUser(dto));
    }

    // ---------- getUserProfile ----------
    @Test
    void getUserProfile_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserProfile(1L);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    // ---------- updateProfile ----------
    @Test
    void updateProfile_success() {
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .userId(1L)
                .name("New Alice")
                .email("new@example.com")
                .bio("New Bio")
                .build();


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponseDTO response = userService.updateProfile(dto);

        assertEquals("New Alice", response.getName());
        assertEquals("new@example.com", response.getEmail());
        assertEquals("New Bio", user.getBio()); // internal update check
    }

    @Test
    void updateProfile_userNotFound() {
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .name("Test")
                .email("test@example.com")
                .bio("Bio")
                .build();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateProfile(dto));
    }

    // ---------- followOrUnfollow ----------
    @Test
    void followOrUnfollow_follow_success() {
        User user2 = User.builder().name("Bob").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        boolean result = userService.followOrUnfollow(1L, 2L);

        assertTrue(result);
        assertTrue(user.getFollowing().contains(user2));
    }

    @Test
    void followOrUnfollow_unfollow_success() {
        User user2 = User.builder().name("Bob").build();
        user.getFollowing().add(user2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        boolean result = userService.followOrUnfollow(1L, 2L);

        assertTrue(result);
        assertFalse(user.getFollowing().contains(user2));
    }

    @Test
    void followOrUnfollow_selfFollow_notAllowed() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.followOrUnfollow(1L, 1L));
    }

    @Test
    void followOrUnfollow_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.followOrUnfollow(1L, 2L));
    }
}