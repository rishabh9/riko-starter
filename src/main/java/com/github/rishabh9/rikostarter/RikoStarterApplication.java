package com.github.rishabh9.rikostarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.github.rishabh9.rikostarter"})
public class RikoStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RikoStarterApplication.class, args);
    }
}
