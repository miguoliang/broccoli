package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.DELETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import broccoli.common.BaseKeycloakTest;
import broccoli.common.IdentityTestHelper;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The {@link UserDeletionTest} class.
 */
class UserDeletionTest extends BaseKeycloakTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  @Test
  void shouldReturnNoContent_WhenUserExists(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = helper.userId(username);

    // Execute
    final var response = client.toBlocking().exchange(DELETE("api/identity/user/" + userId));

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus(), "Status should be NO_CONTENT");

    // Verify user just deleted
    assertFalse(helper.userExists(username), "User should not exist");
  }

  @Test
  void shouldReturnNoContent_WhenUserDoesNotExist() {

    // Setup
    final var userId = "foo";

    // Execute
    final var response = client.toBlocking().exchange(DELETE("api/identity/user/" + userId));


    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus(), "Status should be NO_CONTENT");
  }
}
