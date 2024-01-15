package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;

import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The {@link UserResetPasswordTest} class.
 */
class UserResetPasswordTest extends GeneralTestSetup {

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  @Override
  void beforeAll() throws IOException {

    super.beforeAll();

    final var adminRepresentation =
        keycloak.realm(DEFAULT_ADMIN_REALM).users().search("admin").getFirst();
    final var adminId = adminRepresentation.getId();
    adminRepresentation.setEmail("admin@example.com");
    keycloak.realm(DEFAULT_ADMIN_REALM).users().get(adminId)
        .update(adminRepresentation);

    final var representation = keycloak.realm(DEFAULT_ADMIN_REALM).toRepresentation();
    representation.setEditUsernameAllowed(true);
    representation.setSmtpServer(Map.of(
        "replyToDisplayName", "",
        "starttls", "false",
        "auth", "",
        "port", "1025",
        "replyTo", "",
        "host", "mail",
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
    final var username = identityTestHelper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = identityTestHelper.userId(username);
    identityTestHelper.userEmail(userId, DigestUtils.md5Hex(userId) + "@example.com");

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
