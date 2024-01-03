package broccoli.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.IdentityTestHelper;
import broccoli.model.identity.http.request.CreateUserRequest;
import broccoli.model.identity.http.response.CreateUserResponse;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link UserControllerTest} class.
 */
@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest implements TestPropertyProvider {

  @Container
  static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:23.0.0")
      .withContextPath("/auth")
      .withReuse(true);

  static {
    keycloak.start();
  }

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  @Test
  void testCreateUser_WhenUserDoesNotExist_ShouldReturnUserCreated(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    final var createUserRequest =
        new CreateUserRequest(username, password, "first", "last", "foo@example.com");

    // Execute
    final var response = client.toBlocking()
        .exchange(POST("/identity/user", createUserRequest), CreateUserResponse.class);

    // Verify http response
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatus());

    // Verify http response body
    final var user = response.body();
    assertNotNull(user);
    assertTrue(StringUtils.isNotBlank(user.id()));
    assertEquals(username, user.username());
    assertEquals("first", user.firstName());
    assertEquals("last", user.lastName());
    assertEquals("foo@example.com", user.email());

    // Verify user just created
    assertTrue(helper.userExists(username));
  }

  @Override
  public @NonNull Map<String, String> getProperties() {
    return Map.of(
        "micronaut.security.enabled", "false",
        "keycloak.admin-client.server-url", keycloak.getAuthServerUrl(),
        "keycloak.default.realm", "master"
    );
  }
}
