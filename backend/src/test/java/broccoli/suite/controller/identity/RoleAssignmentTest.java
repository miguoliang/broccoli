package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.BaseKeycloakTest;
import broccoli.common.IdentityTestHelper;
import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.keycloak.admin.client.Keycloak;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link RoleAssignmentTest} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
class RoleAssignmentTest extends BaseKeycloakTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  @Inject
  Keycloak keycloak;

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  RoleAssignmentTest() {
    super();
  }

  @Test
  void shouldReturnNoContent_WhenUserAlreadyHasSpecificRole(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = helper.userId(username);
    final var roleId = helper.roleId("user");
    fluentTestsHelper.assignRoleWithUser(username, "user");

    // Execute
    final var response =
        client.toBlocking().exchange(POST("api/identity/user/" + userId + "/role/" + roleId, null));

    // Verify http response
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus(), "Status should be NO_CONTENT");

    // Verify user role
    final var userRoles = helper.userRoles(userId);
    assertNotNull(userRoles, "User should have role");
    assertTrue(userRoles.contains("user"), "User should have role");
  }

  @Test
  void shouldReturnNotFound_WhenUserExistsAndRoleDoesNotExist(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var userId = helper.userId(username);
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
    final var userRoles = helper.userRoles(userId);
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
