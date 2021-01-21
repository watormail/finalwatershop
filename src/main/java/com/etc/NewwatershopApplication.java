package com.etc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)

public class NewwatershopApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewwatershopApplication.class, args);
    }

}
