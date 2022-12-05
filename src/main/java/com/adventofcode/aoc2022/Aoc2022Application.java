package com.adventofcode.aoc2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Aoc2022Application {

    public static void main(String[] args) {
        SpringApplication.run(Aoc2022Application.class, args);
    }

    @Bean
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }
}
