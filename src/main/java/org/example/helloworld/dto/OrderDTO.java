package org.example.helloworld.dto;

import org.example.helloworld.model.OrderStatus;
import java.io.Serializable;

public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String username;
    private OrderStatus status;
    private double totalPrice;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Long userId, String username, OrderStatus status, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
