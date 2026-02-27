package org.example.helloworld.dto;

import java.io.Serializable;
import java.util.List;

public class MenuDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Double totalPrice;
    private List<DishDTO> dishes;

    public MenuDTO() {}

    public MenuDTO(Long id, String name, String description,
                   Double totalPrice, List<DishDTO> dishes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalPrice = totalPrice;
        this.dishes = dishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<DishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes;
    }
}
