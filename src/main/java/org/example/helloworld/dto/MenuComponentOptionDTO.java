package org.example.helloworld.dto;


import java.io.Serializable;

public class MenuComponentOptionDTO implements Serializable {

    public enum Type { DISH, MENU }

    private Long id;
    private String name;
    private Type type;

    public MenuComponentOptionDTO() {
    }

    public MenuComponentOptionDTO(Long id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
