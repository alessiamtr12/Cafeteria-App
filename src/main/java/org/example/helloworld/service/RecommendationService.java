package org.example.helloworld.service;

import org.example.helloworld.model.MenuComponent;
import org.example.helloworld.model.User;
import org.example.helloworld.repository.MenuComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private MenuComponentRepository componentRepository;
    public List<MenuComponent> recommendationBasedOnFavorites(User user, int limit) {

        List<MenuComponent> allItems = componentRepository.findAll();

        Set<String> userTasteProfile = user.getFavoriteItems().stream()
                .flatMap(item -> extractKeywords(item).stream())
                .collect(Collectors.toSet());

        if (userTasteProfile.isEmpty()) return Collections.emptyList();

        return allItems.stream()

                .filter(item -> user.getFavoriteItems().stream()
                        .noneMatch(fav -> fav.getId().equals(item.getId())))
                .map(item -> new ScoredItem(item, calculateSimilarity(userTasteProfile, extractKeywords(item))))
                .filter(scored -> scored.score > 0)
                .sorted(Comparator.comparingDouble((ScoredItem s) -> s.score).reversed())
                .limit(limit)
                .map(scored -> scored.item)
                .collect(Collectors.toList());
    }
    private static class ScoredItem {
        MenuComponent item;
        double score;
        ScoredItem(MenuComponent item, double score) { this.item = item; this.score = score; }
    }
    private List<String> extractKeywords(MenuComponent item) {
        String content = (item.getName() + " " + item.getDescription()).toLowerCase();
        return Arrays.asList(content.split("[\\s,.]+"));
    }

    private double calculateSimilarity(Set<String> userProfile, List<String> keywords) {
        long matches = keywords.stream().filter(keyword -> userProfile.contains(keyword)).count();
        if (keywords.isEmpty())
            return 0.0;
        return (double) matches / (userProfile.size() + keywords.size() - matches);
    }
}
