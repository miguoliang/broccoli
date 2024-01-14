package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.common.AbstractKeycloakBasedTest;
import broccoli.common.IdentityTestHelper;
import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link UserResetPasswordTest} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
class UserResetPasswordTest extends AbstractKeycloakBasedTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  @BeforeAll
  public static void setup() {

    final var adminRepresentation =
        KEYCLOAK_ADMIN_CLIENT.realm("master").users().search("admin").getFirst();
    final var adminId = adminRepresentation.getId();
    adminRepresentation.setEmail("admin@example.com");
    KEYCLOAK_ADMIN_CLIENT.realm("master").users().get(adminId).update(adminRepresentation);

    final var representation = KEYCLOAK_ADMIN_CLIENT.realm("master").toRepresentation();
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
    KEYCLOAK_ADMIN_CLIENT.realm("master").update(representation);
  }

  @Test
  void shouldReturnNoContent_WhenUserExists(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = helper.userId(username);
    helper.userEmail(userId, DigestUtils.md5Hex(userId) + "@example.com");

    // Execute
    final var response =
        client.toBlocking().exchange(POST("api/identity/user/" + userId + "/reset-password", null));

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus(), "Status should be NO_CONTENT");
  }

  @Test
  void shouldReturnNotFound_WhenUserDoesNotExist() {

    // Setup
    final var userId = "non-exist-user-id";

    // Execute
    final var thrown = assertThrowsExactly(HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(POST("api/identity/user/" + userId + "/reset-password", null)),
        "User does not exist");

    // Verify
    assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus(), "Status should be NOT_FOUND");
  }
}
