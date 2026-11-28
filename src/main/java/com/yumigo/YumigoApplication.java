package com.yumigo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class YumigoApplication {

    public static void main(String[] args) {

        SpringApplication.run(YumigoApplication.class, args);
    }
}