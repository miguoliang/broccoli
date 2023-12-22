package broccoli;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BroccoliTest implements TestPropertyProvider {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withReuse(true);

    @Test
    void testItWorks() {
        Assertions.assertTrue(true);
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!container.isRunning()) {
            container.start();
        }
        return Map.of(
                "jpa.default.properties.hibernate.connection.url", container.getJdbcUrl()
        );
    }
}
