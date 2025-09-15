package com.Curio.Controllers;

import com.Curio.DTOs.LoginDTO;
import com.Curio.DTOs.RegisterDTO;
import com.Curio.DTOs.UpdateProfileDTO;
import com.Curio.Models.User;
import com.Curio.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterDTO request){
        return  userService.createUser(request);
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
    public User update(UpdateProfileDTO request){
        return userService.updateProfile(request);
    }

    @PostMapping("/{user1}/follow_unfollow/{user2}")
    public boolean followOrUnfollow(@RequestParam Long user1, @RequestParam Long user2){
        return userService.followOrUnfollow(user1, user2);
    }
}
