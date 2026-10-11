package com.smartstoragebin.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartStorageBinApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartStorageBinApplication.class, args);
    }
}
