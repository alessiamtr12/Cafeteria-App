package org.example.helloworld.service;

import org.example.helloworld.model.User;
import org.example.helloworld.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository repo, PasswordEncoder passwordEncoder){
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password){
        if(repo.existsByUsername(username)){
            throw new RuntimeException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User.Builder(username, encodedPassword)
                .withRole("CUSTOMER")
                .build();
        repo.save(user);
    }

}
