package ru.shop_example.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop_example.user_service.entity.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName (String name);
}
