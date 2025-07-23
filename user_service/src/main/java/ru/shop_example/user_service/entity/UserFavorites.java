package ru.shop_example.user_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Список избранного пользователя в Монго.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user_favorites")
public class UserFavorites {

    /** Id пользователя. */
    @Id
    private UUID userId;

    /** Время последнего изменения. */
    @LastModifiedDate
    private Instant lastChange;

    /** Избранные товары. */
    private Set<UUID> values;
}
