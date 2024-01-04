package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_TEST_REALM;

import broccoli.common.AbstractKeycloakBasedTest;
import broccoli.common.IdentityTestHelper;
import broccoli.model.identity.http.request.CreateUserRequest;
import broccoli.model.identity.http.response.CreateUserResponse;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.keycloak.test.FluentTestsHelper;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link UserCreationTest} class.
 */
@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserCreationTest extends AbstractKeycloakBasedTest implements TestPropertyProvider {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  FluentTestsHelper fluentTestsHelper;

  @BeforeAll
  void setup() {
    fluentTestsHelper = new FluentTestsHelper(
        KEYCLOAK_CONTAINER.getAuthServerUrl(),
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_CLIENT,
        DEFAULT_TEST_REALM
    );
    fluentTestsHelper.init();
  }

  @Test
  void shouldReturnUserCreated_WhenUserDoesNotExist(TestInfo testInfo) {

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
    assertNotNull(user, "User created");
    assertTrue(StringUtils.isNotBlank(user.id()), "User id is not blank");
    assertEquals(username, user.username(), "Username matches");
    assertEquals("first", user.firstName(), "First name matches");
    assertEquals("last", user.lastName(), "Last name matches");
    assertEquals("foo@example.com", user.email(), "Email matches");

    // Verify user just created
    assertTrue(helper.userExists(username), "User exists");
  }

  @Test
  void shouldReturnConflict_WhenUserAlreadyExists(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);
    final var createUserRequest =
        new CreateUserRequest(username, password, "first", "last", "foo@example.com");

    // Execute
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(POST("/identity/user", createUserRequest), CreateUserResponse.class),
        "User already exists");

    // Verify
    assertEquals(HttpStatus.CONFLICT, thrown.getStatus(), "Status should be CONFLICT");
  }

  @Override
  public @NonNull Map<String, String> getProperties() {

    return Map.of(
        "micronaut.security.enabled", "false",
        "keycloak.admin-client.server-url", KEYCLOAK_CONTAINER.getAuthServerUrl(),
        "keycloak.default.realm", "master"
    );
  }
}
