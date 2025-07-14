package ru.shop_example.user_service.common.containers;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public interface MongoContainer {

    @Container
    @ServiceConnection
    MongoDBContainer MONGO =
            new MongoDBContainer("mongo:4.4")
                    .withCopyFileToContainer(MountableFile.forHostPath("mongo/"), "/docker-entrypoint-initdb.d")
                    .withEnv("MONGO_INITDB_DATABASE", "example_app");
}
