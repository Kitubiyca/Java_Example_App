package ru.shop_example.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.shop_example.user_service.configuration.TokenProperties;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
@EnableFeignClients
@EnableConfigurationProperties(TokenProperties.class)
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
