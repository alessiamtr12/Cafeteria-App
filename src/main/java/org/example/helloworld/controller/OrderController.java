package org.example.helloworld.controller;

import org.example.helloworld.model.MenuComponent;
import org.example.helloworld.model.Order;
import org.example.helloworld.model.OrderStatus;
import org.example.helloworld.model.User;
import org.example.helloworld.repository.MenuComponentRepository;
import org.example.helloworld.repository.OrderRepository;
import org.example.helloworld.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuComponentRepository menuComponentRepository;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository, MenuComponentRepository menuComponentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.menuComponentRepository = menuComponentRepository;
    }

    @PostMapping("/add")
    @ResponseBody
    public String addToOrder(@RequestParam Long itemId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(OrderStatus.PENDING);
                    return newOrder;
                });

        MenuComponent item = menuComponentRepository.findById(itemId).orElseThrow();
        order.addItem(item);
        orderRepository.save(order);

        return item.getName();
    }

    @GetMapping("/view")
    public String viewCart(Model model, Principal principal) {
        if (principal == null) return "redirect:/auth";

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order activeOrder = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING).orElse(null);
        List<Order> orderHistory = orderRepository.findAllByUserAndStatus(user, OrderStatus.COMPLETED);

        model.addAttribute("activeOrder", activeOrder);
        model.addAttribute("orderHistory", orderHistory);
        model.addAttribute("userId", user.getId());

        return "cart";
    }

    @PostMapping("/remove")
    public String removeFromOrder(@RequestParam Long itemId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("No active tray found"));

        MenuComponent itemToRemove = menuComponentRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        order.getItems().remove(itemToRemove);

        if (order.getItems().isEmpty()) {
            orderRepository.delete(order);
        } else {
            orderRepository.save(order);
        }

        return "redirect:/orders/view";
    }

    @PostMapping("/place")
    public String placeOrder(Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("No active tray found"));

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");

        return "redirect:/orders/view";
    }
}