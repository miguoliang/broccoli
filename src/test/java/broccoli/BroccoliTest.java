package broccoli;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
class BroccoliTest implements TestPropertyProvider {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!container.isRunning()) {
            container.start();
        }
        return Map.of("jpa.default.properties.hibernate.connection.url", container.getJdbcUrl(),
                "jpa.default.properties.hibernate.connection.username", container.getUsername(),
                "jpa.default.properties.hibernate.connection.password", container.getPassword());
    }
}
