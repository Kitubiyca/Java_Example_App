package ru.shop_example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop_example.user_service.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с доменной сущностью пользователя.
 *
 * @see User
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Найти пользователя по электронной почте.
     *
     * @param email электронная почта
     *
     * @return Optional с пользователем
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Проверить существует ли пользователь с указанной электронной почтой.
     *
     * @param email электронная почта
     *
     * @return существует ли пользователь
     */
    Boolean existsByEmail(String email);

    /**
     * Проверить существует ли пользователь с указанным номером телефона.
     *
     * @param phoneNumber номер телефона
     *
     * @return существует ли пользователь
     */
    Boolean existsByPhoneNumber(String phoneNumber);
}
