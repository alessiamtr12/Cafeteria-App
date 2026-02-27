package org.example.helloworld.controller;

import org.example.helloworld.model.User;
import org.example.helloworld.repository.UserRepository;
import org.example.helloworld.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/auth")
    public String authPage() {
        return "auth";
    }


    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("showRegister", true);
        return "auth";
    }


    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {

        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("registerError", "Username and password are required");
            model.addAttribute("showRegister", true);
            return "auth";
        }

        try {
            registrationService.register(username, password);
            model.addAttribute("registerSuccess", "Registration successful! You can now log in.");
        } catch (RuntimeException e) {
            model.addAttribute("registerError", e.getMessage());
            model.addAttribute("showRegister", true);
        }

        return "auth";
    }


}