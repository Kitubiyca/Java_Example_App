package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO с id избранных товаров")
public class ResponseUserFavoritesDto {

    @ArraySchema(
            schema = @Schema(description = "Id товара", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229", implementation = UUID.class),
            arraySchema = @Schema(description = "Список товаров", example = "[\\\"323e33e1-18f1-4bdf-b6b7-42db966c2229\\\", \\\"722ff576-aee7-465f-9c88-3d1d9d6951c0\\\", \\\"a9fbdfda-4a3c-404d-b7b1-f7d02c9bee60\\\"]"),
            uniqueItems = true
    )
    private Set<UUID> values;
}
