package ru.shop_example.user_service.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenApi.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "user-service",
                version = "v1.0",
                description = "example_shop project user service API doc"
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Local"),
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfiguration {

    /**
     * Устанавливает на все эндпоинты стандартные ответы 500 и 400, чтобы не рпописывать их везде вручную.
     *
     * @return кастомизатор OpenApi документа
     */
    @Bean
    public OpenApiCustomizer addGlobalResponses() {
        return openApi -> {
            Schema<?> schema = new Schema<>().$ref("#/components/schemas/ResponseErrorDto");
            Content content = new Content().addMediaType("application/json", new MediaType().schema(schema));
            ApiResponse serverError = new ApiResponse().description("Ошибка сервера").content(content);
            ApiResponse badRequest = new ApiResponse().description("Неверный запрос").content(content);
            openApi.getPaths().values().forEach(pathItem -> {
                pathItem.readOperations().forEach(op -> {
                    if (!op.getResponses().containsKey("500")) {
                        op.getResponses().addApiResponse("500", serverError);
                    }
                    if (!op.getResponses().containsKey("400")) {
                        op.getResponses().addApiResponse("400", badRequest);
                    }
                });
            });
        };
    }

    /**
     * Делает хэдер user-id необязательным в запросах через swagger ui, так как его проставляет гейтвей.
     *
     * @return кастомизатор операции на uri
     */
    @Bean
    public OperationCustomizer removeUserIdHeader() {
        return (operation, handlerMethod) -> {
            if (operation.getParameters() != null) {
                for (Parameter p : operation.getParameters()) {
                    if ("user-id".equals(p.getName())) {
                        p.setRequired(false);
                    }
                }
            }
            return operation;
        };
    }
}
