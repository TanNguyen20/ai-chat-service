package com.tannguyen.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AiChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiChatServiceApplication.class, args);
    }

}
