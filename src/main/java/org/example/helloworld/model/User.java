package org.example.helloworld.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String email;
    private String phone;
    private double balance;
    private String role;

    @ElementCollection
    @CollectionTable(name = "user_allergies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "allergy")
    private List<String> allergies = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "component_id")
    )
    private List<MenuComponent> favoriteItems = new ArrayList<>();

    private User(Builder builder){
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.phone = builder.phone;;
        this.balance = builder.balance;
        this.role = builder.role;
        this.allergies = builder.allergies;
        this.favoriteItems = builder.favoriteItems;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFavoriteItems(List<MenuComponent> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public User() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static class Builder{
        private final String username;
        private final String password;

        private  String email = "";
        private  String phone = "";
        private  double balance = 0.0;
        private String role = "customer";
        private  List<String> allergies = new ArrayList<>();
        private List<MenuComponent> favoriteItems = new ArrayList<>();



        public Builder(String username, String password){
            this.username = username;
            this.password = password;
        }

        public Builder withEmail(String email){
            this.email = email;
            return this;
        }

        public Builder withPhone(String phone){
            this.phone = phone;
            return this;
        }

        public Builder withBalance(double balance){
            this.balance = balance;
            return this;
        }

        public Builder withRole(String role){
            this.role = role;
            return this;
        }

        public Builder withAllergy(String allergy){
            this.allergies.add(allergy);
            return this;
        }

        public Builder withFavorite(MenuComponent favorite){
            this.favoriteItems.add(favorite);
            return this;
        }

        public User build(){
            return new User(this);
        }
    }

    public List<MenuComponent> getFavoriteItems() {
        return favoriteItems;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public String getRole() {
        return role;
    }

    public double getBalance() {
        return balance;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
    @Override
    public String toString(){
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", balance=" + balance +
                ", allergies=" + allergies +
                '}';
    }
}
