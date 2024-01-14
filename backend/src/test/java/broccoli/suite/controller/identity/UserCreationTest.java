package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.BaseKeycloakTest;
import broccoli.common.IdentityTestHelper;
import broccoli.model.identity.http.request.CreateUserRequest;
import broccoli.model.identity.http.response.CreateUserResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.StringUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link UserCreationTest} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
class UserCreationTest extends BaseKeycloakTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  UserCreationTest() {
    super();
  }

  @Test
  void shouldReturnUserCreated_WhenUserDoesNotExist(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
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
    assertTrue(helper.userExists(username), "User exists");
  }

  @Test
  void shouldReturnConflict_WhenUserAlreadyExists(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
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
}
