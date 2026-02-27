package org.example.helloworld.controller;

import org.example.helloworld.model.User;
import org.example.helloworld.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(@RequestParam Long userId, Model model) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam Long userId,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam double balance,
                                Model model) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(email);
        user.setPhone(phone);
        user.setBalance(balance);

        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("success", "Profile updated successfully");

        return "profile";
    }
}
