package com.akdpro.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoreOkmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreOkmApplication.class, args);
    }

}
