package com.jazztech.cardholder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@EnableJpaRepositories
@SpringBootApplication
public class CardHolderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardHolderApplication.class, args);
    }

}
