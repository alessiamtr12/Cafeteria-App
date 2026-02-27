package org.example.helloworld.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @ManyToMany
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "component_id")
    )
    private List<MenuComponent> items = new ArrayList<>();

    public double getTotalPrice() {
        double total = 0;
        for (MenuComponent item : items) {
            total += item.computePrice();
        }
        return total;
    }

    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<MenuComponent> getItems() {
        return items;
    }
    public void addItem(MenuComponent item) {
        this.items.add(item);
    }
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}