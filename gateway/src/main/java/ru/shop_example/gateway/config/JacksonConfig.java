package ru.shop_example.gateway.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shop_example.gateway.deserializer.SecuritySchemeTypeDeserializer;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customSecuritySchemeTypeDeserializer() {
        return builder -> {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(
                    SecurityScheme.Type.class,
                    new SecuritySchemeTypeDeserializer()
            );
            builder.modules(module, new JavaTimeModule());
            //builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}
