package broccoli.filters.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.adapters.authorization.PolicyEnforcer;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.test.FluentTestsHelper;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.keycloak.test.FluentTestsHelper.*;

@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PolicyEnforcerFilterTest implements TestPropertyProvider {

    @Container
    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:23.0.0")
        .withRealmImportFile("realm-export.json")
        .withReuse(true);

    static String getJwksUri() {
        return String.format("http://%s", keycloak.getHost() + ":" + keycloak.getFirstMappedPort() + "/auth/realms/quickstart/protocol/openid-connect/certs");
    }

    @Inject
    @Client("/")
    HttpClient client;

    FluentTestsHelper testsHelper;

    @BeforeAll
    public void setup() throws IOException {
        testsHelper = new FluentTestsHelper(
            keycloak.getAuthServerUrl(),
            DEFAULT_ADMIN_USERNAME,
            DEFAULT_ADMIN_PASSWORD,
            DEFAULT_ADMIN_REALM,
            DEFAULT_ADMIN_CLIENT,
            "quickstart"
        );
        testsHelper.init();
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!keycloak.isRunning()) {
            keycloak.start();
        }
        return Map.of(
            "micronaut.security.oauth2.clients.default.openid.jwks-uri", getJwksUri()
        );
    }

    @Test
    void testItWorks() {

        final var code = client.toBlocking().exchange(HttpRequest.GET("/vertex")).status().getCode();
        assertEquals(401, code);
    }

    @MockBean(PolicyEnforcerConfig.class)
    PolicyEnforcerConfig policyEnforcerConfig() {

        final var enforcerConfig = new PolicyEnforcerConfig();
        enforcerConfig.setAuthServerUrl(keycloak.getAuthServerUrl());
        enforcerConfig.setRealm("quickstart");
        enforcerConfig.setResource("authz-servlet");
        enforcerConfig.setCredentials(Map.of("secret", "secret"));
        return enforcerConfig;
    }
}
