package org.example.helloworld.controller;

import org.example.helloworld.model.MenuComponent;
import org.example.helloworld.model.User;
import org.example.helloworld.repository.UserRepository;
import org.example.helloworld.service.RecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public RecommendationController(RecommendationService recommendationService, UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getRecommendations(Model model, java.security.Principal principal) {
        if (principal == null) {
            return "redirect:/auth";
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<MenuComponent> recs = recommendationService.recommendationBasedOnFavorites(user, 5);

        model.addAttribute("recommendations", recs);
        model.addAttribute("userFavorites", user.getFavoriteItems());
        model.addAttribute("userId", user.getId());

        return "recommendations";
    }
}