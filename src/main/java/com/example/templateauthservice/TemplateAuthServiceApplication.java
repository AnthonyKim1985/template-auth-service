package com.example.templateauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2WebFlux
@EnableMongoAuditing
@EnableReactiveMongoRepositories
public class TemplateAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemplateAuthServiceApplication.class, args);
    }
}
