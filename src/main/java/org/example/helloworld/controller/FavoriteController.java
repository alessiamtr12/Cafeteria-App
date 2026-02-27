package org.example.helloworld.controller;

import org.example.helloworld.model.MenuComponent;
import org.example.helloworld.model.User;
import org.example.helloworld.repository.MenuComponentRepository;
import org.example.helloworld.repository.UserRepository;
import org.springframework.stereotype.Controller; // Changed
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final UserRepository userRepository;
    private final MenuComponentRepository menuComponentRepository;

    public FavoriteController(UserRepository userRepository, MenuComponentRepository menuComponentRepository) {
        this.userRepository = userRepository;
        this.menuComponentRepository = menuComponentRepository;
    }

    @PostMapping("/toggle/{itemId}")
    @ResponseBody
    public void toggleFavorite(@PathVariable Long itemId,
                               java.security.Principal principal) {

        if (principal == null) {
            throw new RuntimeException("You must be logged in to favorite items");
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MenuComponent item = menuComponentRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (user.getFavoriteItems().contains(item)) {
            user.getFavoriteItems().remove(item);
        } else {
            user.getFavoriteItems().add(item);
        }

        userRepository.save(user);
    }
}