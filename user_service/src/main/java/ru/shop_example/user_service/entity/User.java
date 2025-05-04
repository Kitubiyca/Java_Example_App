package ru.shop_example.user_service.entity;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import ru.shop_example.user_service.entity.constant.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

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

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "firstname", length = 40, nullable = false)
    private String firstname;

    @Column(name = "lastname", length = 40, nullable = false)
    private String lastname;

    @Column(name = "patronymic", length = 40)
    private String patronymic;

    @Column(name = "phone_number", length = 12, nullable = false)
    private String phoneNumber;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "access_date", nullable = false)
    private OffsetDateTime accessDate;

    @Column(name = "registration_date", nullable = false)
    private OffsetDateTime registrationDate;

    @Column(name = "status", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}