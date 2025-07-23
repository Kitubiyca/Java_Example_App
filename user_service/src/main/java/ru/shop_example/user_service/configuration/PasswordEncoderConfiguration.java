package ru.shop_example.user_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Класс конфигурации кодировщика паролей.
 */
@Configuration
public class PasswordEncoderConfiguration {

    /**
     * Создаёт бин кодировщика паролей.
     *
     * @return сам кодировщик паролей
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoderBean(){
        return new BCryptPasswordEncoder();
    }
}
