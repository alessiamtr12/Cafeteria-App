package org.example.helloworld.controller;

import org.example.helloworld.model.User;
import org.example.helloworld.repository.MenuComponentRepository;
import org.example.helloworld.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final MenuComponentRepository repository;
    private final UserRepository userRepository;

    public MainController(MenuComponentRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(Model model, java.security.Principal principal) {
        model.addAttribute("items", repository.findAll());
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("userId", user.getId());
                model.addAttribute("userFavorites", user.getFavoriteItems());
            }
        }
        return "index";
    }
}