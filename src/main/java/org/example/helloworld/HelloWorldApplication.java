package org.example.helloworld;

import org.example.helloworld.model.Dish;
import org.example.helloworld.model.Menu;
import org.example.helloworld.model.User;
import org.example.helloworld.repository.DishRepository;
import org.example.helloworld.repository.MenuRepository;
import org.example.helloworld.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@EntityScan(basePackages = "org.example.helloworld.model")
@EnableJpaRepositories(basePackages = "org.example.helloworld.repository")
public class HelloWorldApplication {

    private final Environment environment;

    public HelloWorldApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }
    @EventListener(ApplicationReadyEvent.class)
    public void printApplicationUrls() {
        String port = environment.getProperty("local.server.port", "8080");
        System.out.println("--------------------------------------------------");
        System.out.println("  Application is running! Access URLs:");
        System.out.println("  Local: \t\thttp://localhost:" + port + "/");
        System.out.println("--------------------------------------------------");
    }

}
