package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.PUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.model.identity.http.request.UpdateUserRequest;
import broccoli.model.identity.http.response.UpdateUserResponse;
import broccoli.security.keycloak.PolicyEnforcerRuleTest;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.testcontainers.junit.jupiter.Container;

/**
 * The {@link UserUpdateTest} class.
 */
class UserUpdateTest extends GeneralTestSetup {

  @Container
  static KeycloakContainer KEYCLOAK_CONTAINER =
      new KeycloakContainer(PolicyEnforcerRuleTest.IMAGE_NAME)
          .withContextPath("/auth")
          .withReuse(true);

  static {
    KEYCLOAK_CONTAINER.start();
  }

  @Test
  void shouldReturnUserUpdated_WhenUpdateUsername_And_NewUsernameDoesNotExist(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = identityTestHelper.userId(username);
    final var newUsername = username + "_1";
    final var updateRequest = new UpdateUserRequest(newUsername, null, null, null);

    // Execute
    final var response = client.toBlocking()
        .exchange(PUT("api/identity/user/" + userId, updateRequest), UpdateUserResponse.class);

    // Verify
    final var updatedUser = response.body();
    assertNotNull(updatedUser);
    assertEquals(newUsername.toLowerCase(), updatedUser.username());
    assertEquals(userId, updatedUser.id());
  }

  @Test
  void shouldReturnConflict_WhenUpdateUsername_And_NewUsernameAlreadyExists(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = identityTestHelper.userId(username);
    final var newUsername = username + "_1";
    fluentTestsHelper.createTestUser(newUsername, password);
    final var updateRequest = new UpdateUserRequest(newUsername, null, null, null);

    // Execute
    final var response = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(PUT("api/identity/user/" + userId, updateRequest), UpdateUserResponse.class),
        "Should return conflict");

    // Verify
    assertEquals(HttpStatus.CONFLICT, response.getStatus());
  }

  @Override
  protected String getAuthServerUrl() {
    return KEYCLOAK_CONTAINER.getAuthServerUrl();
  }
}
