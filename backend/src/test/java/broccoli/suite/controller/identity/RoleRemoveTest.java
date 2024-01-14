package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.DELETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.security.keycloak.PolicyEnforcerRuleTest;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.testcontainers.junit.jupiter.Container;

/**
 * The {@link RoleRemoveTest} class.
 */
class RoleRemoveTest extends GeneralTestSetup {

  @Container
  static KeycloakContainer KEYCLOAK_CONTAINER =
      new KeycloakContainer(PolicyEnforcerRuleTest.IMAGE_NAME)
          .withContextPath("/auth")
          .withReuse(true);

  static {
    KEYCLOAK_CONTAINER.start();
  }

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  @Test
  void shouldReturnNoContent_WhenBothUserAndRoleExists(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = identityTestHelper.userId(username);
    final var roleId = identityTestHelper.roleId("user");
    fluentTestsHelper.assignRoleWithUser(username, "user");

    // Execute
    final var response = client.toBlocking()
        .exchange(DELETE("api/identity/user/" + userId + "/role/" + roleId, null));

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus(), "Status should be NO_CONTENT");

    // Verify user role
    final var userRoles = identityTestHelper.userRoles(userId);
    assertNotNull(userRoles);
    assertEquals(1, userRoles.size(), "User should have only one role");
    assertTrue(userRoles.contains("default-roles-master"), "User should have default role");
  }

  @Test
  void shouldReturnNotFound_WhenUserExistsAndRoleDoesNotExist(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = identityTestHelper.userId(username);
    final var roleId = "non-exist-role-id";

    // Execute
    final var response = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(DELETE("api/identity/user/" + userId + "/roleId/" + roleId, null)),
        "Role does not exist");

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus(), "Status should be NOT_FOUND");

    // Verify user role
    final var userRoles = identityTestHelper.userRoles(userId);
    assertNotNull(userRoles);
    assertEquals(1, userRoles.size(), "User should have only one role");
    assertEquals("default-roles-master", userRoles.getFirst(), "User should have default role");
  }

  @Test
  void shouldReturnNotFound_WhenUserDoesNotExist() {

    // Setup
    final var userId = "non-exist-user-id";
    final var roleId = "should-not-matter";

    // Execute
    final var response = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(DELETE("api/identity/user/" + userId + "/role/" + roleId, null)),
        "User does not exist");

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus(), "Status should be NOT_FOUND");
  }

  @Override
  protected String getAuthServerUrl() {

    return KEYCLOAK_CONTAINER.getAuthServerUrl();
  }
}
