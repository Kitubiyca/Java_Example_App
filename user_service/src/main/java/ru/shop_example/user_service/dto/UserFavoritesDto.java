package ru.shop_example.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserFavoritesDto {

    private Set<UUID> values;
}
