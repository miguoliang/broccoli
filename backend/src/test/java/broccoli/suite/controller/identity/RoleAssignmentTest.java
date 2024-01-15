package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The {@link RoleAssignmentTest} class.
 */
class RoleAssignmentTest extends GeneralTestSetup {

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  @Test
  void shouldReturnNoContent_WhenUserAlreadyHasSpecificRole(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = identityTestHelper.userId(username);
    final var roleId = identityTestHelper.roleId("user");
    fluentTestsHelper.assignRoleWithUser(username, "user");

    // Execute
    final var response =
        client.toBlocking().exchange(POST("api/identity/user/" + userId + "/role/" + roleId, null));

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus(), "Status should be NO_CONTENT");

    // Verify user role
    final var userRoles = identityTestHelper.userRoles(userId);
    assertNotNull(userRoles, "User should have role");
    assertTrue(userRoles.contains("user"), "User should have role");
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
            .exchange(POST("api/identity/user/" + userId + "/roleId/" + roleId, null)),
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
            .exchange(POST("api/identity/user/" + userId + "/role/" + roleId, null)),
        "User does not exist");

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus(), "Status should be NOT_FOUND");
  }
}
