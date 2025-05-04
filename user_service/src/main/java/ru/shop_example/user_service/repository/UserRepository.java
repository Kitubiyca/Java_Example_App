package ru.shop_example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop_example.user_service.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);
}
