package org.example.helloworld.repository;

import org.example.helloworld.model.MenuComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuComponentRepository extends JpaRepository<MenuComponent, Long> {
}