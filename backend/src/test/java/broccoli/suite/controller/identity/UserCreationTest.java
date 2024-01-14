package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.model.identity.http.request.CreateUserRequest;
import broccoli.model.identity.http.response.CreateUserResponse;
import broccoli.security.keycloak.PolicyEnforcerRuleTest;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.StringUtils;
import org.testcontainers.junit.jupiter.Container;

/**
 * The {@link UserCreationTest} class.
 */
class UserCreationTest extends GeneralTestSetup {

  @Container
  static KeycloakContainer KEYCLOAK_CONTAINER =
      new KeycloakContainer(PolicyEnforcerRuleTest.IMAGE_NAME)
          .withContextPath("/auth")
          .withReuse(true);

  static {
    KEYCLOAK_CONTAINER.start();
  }

  @Test
  void shouldReturnUserCreated_WhenUserDoesNotExist(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper
        .username(testInfo);
    final var password = "Aa123456789.";
    final var createUserRequest =
        new CreateUserRequest(username, password, "first", "last", null);

    // Execute
    final var response = client.toBlocking()
        .exchange(POST("api/identity/user", createUserRequest), CreateUserResponse.class);

    // Verify http response
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatus());

    // Verify http response body
    final var user = response.body();
    assertNotNull(user, "User created");
    assertTrue(StringUtils.isNotBlank(user.id()), "User id is not blank");
    assertEquals(username, user.username(), "Username matches");
    assertEquals("first", user.firstName(), "First name matches");
    assertEquals("last", user.lastName(), "Last name matches");

    // Verify user just created
    assertTrue(identityTestHelper
        .userExists(username), "User exists");
  }

  @Test
  void shouldReturnConflict_WhenUserAlreadyExists(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper
        .username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var createUserRequest =
        new CreateUserRequest(username, password, "first", "last", null);

    // Execute
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(POST("api/identity/user", createUserRequest), CreateUserResponse.class),
        "User already exists");

    // Verify
    assertEquals(HttpStatus.CONFLICT, thrown.getStatus(), "Status should be CONFLICT");
  }

  @Override
  protected String getAuthServerUrl() {
    return KEYCLOAK_CONTAINER.getAuthServerUrl();
  }
}
