package broccoli;

import dasniko.testcontainers.keycloak.KeycloakContainer;
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
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withReuse(true);

    @Container
    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:23.0.0")
            .withReuse(true);

    static String getJwksUri() {
        return String.format("http://%s", keycloak.getHost() + ":" + keycloak.getFirstMappedPort() + "/auth/realms/broccoli/protocol/openid-connect/certs");
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(true);
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
        if (!keycloak.isRunning()) {
            keycloak.start();
        }
        return Map.of(
                "jpa.default.properties.hibernate.connection.url", postgres.getJdbcUrl(),
                "micronaut.security.oauth2.clients.default.openid.jwks-uri", getJwksUri()
        );
    }
}
