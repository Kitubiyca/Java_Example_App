package ru.shop_example.user_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.shop_example.user_service.configuration.TokenProperties;

@OpenAPIDefinition(
		info = @Info(
				title = "user-service",
				version = "v1.0",
				description = "example_shop project user service API doc"
		)
)
@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(TokenProperties.class)
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}