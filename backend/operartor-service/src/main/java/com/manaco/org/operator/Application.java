package com.manaco.org.operator;

import com.manaco.org.model.Item;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EntityScan(basePackageClasses = Item.class)
@ComponentScan("com.manaco.org")
@EnableMongoRepositories("com.manaco.org.repositories")
@EnableRabbit
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
