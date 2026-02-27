package org.example.helloworld.model;

import javax.persistence.*;

@Entity
@Table(name = "dish")
public class Dish extends MenuComponent{

    private Double price;

    public Dish(String name, String description, double price) {
        super(name, description);
        this.price = price;
    }

    public Dish() {
    }

    public Double getPrice() {
        return price;
    }
    @Override
    public Double computePrice() {
        return this.getPrice();
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
