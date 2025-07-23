package ru.shop_example.user_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * Рекорд с настройками для токенов.
 * <p>
 * Берётся из файла конфигурации по префиксу "token".
 *
 * @param access настройки access токена
 * @param refresh настройки refresh токена
 */
@ConfigurationProperties(prefix = "token")
public record TokenProperties (
        Access access,
        Refresh refresh) {

        /**
         * Настройки access токена.
         *
         * @param ttl Время жизни access токена
         */
        public record Access(@DefaultValue("30m") Duration ttl) {}

        /**
         * Настройки refresh токена.
         *
         * @param ttl Время жизни refresh токена
         */
        public record Refresh(@DefaultValue("7d") Duration ttl) {}
}
