package com.pjcft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pjcft")
public class ActivitiApplication {
    public static void main(String[] args){
        SpringApplication.run(ActivitiApplication.class);
    }
}
