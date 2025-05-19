package com.food_vn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FoodVNApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodVNApplication.class, args);
    }

}
