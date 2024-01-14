package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.PUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.common.BaseKeycloakTest;
import broccoli.common.IdentityTestHelper;
import broccoli.model.identity.http.request.UpdateUserRequest;
import broccoli.model.identity.http.response.UpdateUserResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link UserUpdateTest} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
class UserUpdateTest extends BaseKeycloakTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  UserUpdateTest() {
    super();
  }

  @BeforeAll
  static void beforeAll() {

    final var realmRepresentation = KEYCLOAK_ADMIN_CLIENT.realm("master").toRepresentation();
    realmRepresentation.setEditUsernameAllowed(true);
    KEYCLOAK_ADMIN_CLIENT.realm("master").update(realmRepresentation);
  }

  @Test
  void shouldReturnUserUpdated_WhenUpdateUsername_And_NewUsernameDoesNotExist(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = helper.userId(username);
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
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = helper.userId(username);
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
}
