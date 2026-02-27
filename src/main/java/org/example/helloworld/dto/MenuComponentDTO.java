package org.example.helloworld.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuComponentDTO implements Serializable {

    public enum Type { DISH, MENU }

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Type type;

    private List<MenuComponentDTO> children = new ArrayList<>();

    public MenuComponentDTO() {
    }

    public MenuComponentDTO(Long id, String name, String description, Double price, Type type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Type getType() { return type; }
    public List<MenuComponentDTO> getChildren() { return children; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setType(Type type) { this.type = type; }
    public void setChildren(List<MenuComponentDTO> children) { this.children = children; }
}