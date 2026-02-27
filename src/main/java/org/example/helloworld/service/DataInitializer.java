package org.example.helloworld.service;

import org.example.helloworld.model.User;
import org.example.helloworld.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner migratePasswords(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<User> users = userRepository.findAll();

            for (User user : users) {
                String currentPassword = user.getPassword();

                if (currentPassword != null && !currentPassword.startsWith("$2a$")) {
                    System.out.println("Encrypting password for user: " + user.getUsername());

                    String encodedPassword = passwordEncoder.encode(currentPassword);
                    user.setPassword(encodedPassword);

                    userRepository.save(user);
                }
            }
        };
    }
}