package ru.shop_example.gateway.repository;

import org.springframework.data.repository.CrudRepository;
import ru.shop_example.gateway.entity.ServiceData;

public interface ServiceDataRepository extends CrudRepository<ServiceData, String> {
    void deleteByName(String name);
}
