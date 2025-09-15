package com.Curio.Controllers;

import com.Curio.DTOs.LoginDTO;
import com.Curio.DTOs.RegisterDTO;
import com.Curio.DTOs.UpdateProfileDTO;
import com.Curio.Models.User;
import com.Curio.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterDTO request){
        return  ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(LoginDTO request){
        return userService.authenticateUser(request);
    }

    @GetMapping("/profile/{userId}")
    public Optional<User> getUserProfile(@RequestParam Long userId){
        return userService.getUserProfile(userId);
    }

    @PutMapping("/editProfile")
    public ResponseEntity<User> update(UpdateProfileDTO request){
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PostMapping("/{user1}/follow_unfollow/{user2}")
    public boolean followOrUnfollow(@RequestParam Long user1, @RequestParam Long user2){
        return userService.followOrUnfollow(user1, user2);
    }
}
