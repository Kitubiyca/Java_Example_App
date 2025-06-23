package ru.shop_example.gateway.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SecuritySchemeTypeDeserializer extends StdDeserializer<SecurityScheme.Type> {

    public SecuritySchemeTypeDeserializer() {
        super(SecurityScheme.Type.class);
    }

    public SecurityScheme.Type deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String text = jsonParser.getText();
        try {
            return SecurityScheme.Type.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
