package org.example.helloworld.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu extends MenuComponent {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_menu_id")
    private List<MenuComponent> components = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, String description) {
        super(name, description);
    }

    @Override
    public Double computePrice() {
        double total = 0.0;
        for (MenuComponent component : components) {
            total += component.computePrice();
        }
        return total;
    }

    public void addComponent(MenuComponent component) {
        components.add(component);
    }

    public void removeComponent(MenuComponent component) {
        components.remove(component);
    }

    public List<MenuComponent> getComponents() {
        return components;
    }

    public void setComponents(List<MenuComponent> components) {
        this.components = components;
    }
}