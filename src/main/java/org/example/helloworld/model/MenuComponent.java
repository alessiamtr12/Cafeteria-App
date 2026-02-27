package org.example.helloworld.model;

import javax.persistence.*;

@Entity
@Table(name = "menu_component")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MenuComponent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    public MenuComponent() {
    }

    public MenuComponent(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract Double computePrice();

    public Long getId() {
        return id;
    }
    public Double getPrice() {
        return computePrice();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuComponent)) return false;
        MenuComponent that = (MenuComponent) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}