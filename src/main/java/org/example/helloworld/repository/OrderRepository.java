package org.example.helloworld.repository;

import org.example.helloworld.model.Order;
import org.example.helloworld.model.OrderStatus;
import org.example.helloworld.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUser(User user);
    Optional<Order> findByUserAndStatus(User user, OrderStatus status);
    List<Order> findAllByUserAndStatus(User user, OrderStatus orderStatus);
}