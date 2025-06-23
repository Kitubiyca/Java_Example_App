package ru.shop_example.user_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user_favorites")
public class UserFavorites {

    @Id
    private UUID userId;

    @LastModifiedDate
    private Instant lastChange;

    private Set<UUID> values;
}
