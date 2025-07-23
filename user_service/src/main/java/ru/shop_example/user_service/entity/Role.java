package ru.shop_example.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Роль пользователя в приложении.
 *
 * @see User
 */
@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /** Id роли. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private UUID id;

    /** Имя роли (Заглавными латинскими буквами). */
    @Column(name = "name", length = 40, nullable = false)
    private String name;

    /** Описание роли. */
    @Column(name = "description")
    private String description;
}
