package com.Curio.Services;

import com.Curio.Models.User;
import com.Curio.Repositories.UserRepository;
import com.Curio.Configurations.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + usernameOrEmail));

        // Build our UserPrincipal (implements UserDetails)
        return UserPrincipal.create(user);
    }
}
