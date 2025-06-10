package ru.shop_example.user_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "token")
public record TokenProperties (
        Access access,
        Refresh refresh) {
        public record Access(@DefaultValue("30m") Duration ttl) {}
        public record Refresh(@DefaultValue("7d") Duration ttl) {}
}
