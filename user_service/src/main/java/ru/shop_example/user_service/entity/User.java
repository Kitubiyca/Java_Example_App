package ru.shop_example.user_service.entity;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import ru.shop_example.user_service.entity.constant.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Пользователь приложения.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"phone_number", "email"})
})
@NamedEntityGraph(
        name = "User.withRole",
        attributeNodes = @NamedAttributeNode("role")
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** Id пользователя. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private UUID id;

    /** Электронная почта. */
    @Column(name = "email", length = 50, nullable = false)
    private String email;

    /** Пароль пользователя (зашифрован). */
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    /** Имя. */
    @Column(name = "firstname", length = 40, nullable = false)
    private String firstname;

    /** Фамилия. */
    @Column(name = "lastname", length = 40, nullable = false)
    private String lastname;

    /** Отчество. */
    @Column(name = "patronymic", length = 40)
    private String patronymic;

    /** Номер телефона. */
    @Column(name = "phone_number", length = 12, nullable = false)
    private String phoneNumber;

    /** Дата рождения формата "1990-01-01". */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /** Дата входа в аккаунт формата 2025-06-17T15:31:41.221527506. */
    @Column(name = "access_date", nullable = false)
    private OffsetDateTime accessDate;

    /** Дата регистрации формата 2025-06-17T15:31:41.221527506. */
    @Column(name = "registration_date", nullable = false)
    private OffsetDateTime registrationDate;

    /** Статус пользователя. */
    @Column(name = "status", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /** Роль пользователя. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}