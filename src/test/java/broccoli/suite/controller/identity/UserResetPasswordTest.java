package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import broccoli.common.AbstractKeycloakBasedTest;
import broccoli.common.IdentityTestHelper;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.admin.client.Keycloak;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link UserResetPasswordTest} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserResetPasswordTest extends AbstractKeycloakBasedTest implements TestPropertyProvider {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  @Inject
  Keycloak keycloak;

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  @BeforeAll
  void setup() {

    final var adminId = helper.userId("admin");
    helper.userEmail(adminId, "admin@exmaple.com");

    final var representation = keycloak.realm("master").toRepresentation();
    representation.setSmtpServer(Map.of(
        "replyToDisplayName", "",
        "starttls", "false",
        "auth", "",
        "port", "1025",
        "replyTo", "",
        "host", "mailhog",
        "from", "from@example.com",
        "fromDisplayName", "",
        "envelopeFrom", "",
        "ssl", "false"
    ));
    keycloak.realm("master").update(representation);
  }

  @Test
  void shouldReturnNoContent_WhenUserExists(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = helper.userId(username);
    helper.userEmail(userId, "foo@example.com");


    // Execute
    final var response =
        client.toBlocking().exchange(POST("/identity/user/" + userId + "/reset-password", null));

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus(), "Status should be NO_CONTENT");
  }

  @Override
  public @NonNull Map<String, String> getProperties() {

    return Map.of(
        "micronaut.security.enabled", "false",
        "keycloak.admin-client.server-url", KEYCLOAK_CONTAINER.getAuthServerUrl(),
        "keycloak.default.realm", "master"
    );
  }
}
