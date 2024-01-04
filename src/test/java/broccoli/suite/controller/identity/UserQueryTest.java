package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_TEST_REALM;

import broccoli.common.AbstractKeycloakBasedTest;
import broccoli.common.IdentityTestHelper;
import broccoli.model.identity.http.response.QueryUserResponse;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.test.FluentTestsHelper;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link UserQueryTest} class.
 */
@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserQueryTest extends AbstractKeycloakBasedTest implements TestPropertyProvider {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper helper;

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

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
  void query_ShouldReturnFound_WithQ(TestInfo testInfo) {

    // Setup
    final var username = helper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);

    // Execute
    final var found = client.toBlocking().exchange(GET("identity/user?q=" + username), Argument.of(
        Page.class, QueryUserResponse.class));

    // Verify response status
    assertNotNull(found, "Response should not be null");
    assertEquals(HttpStatus.OK, found.getStatus(), "Status should be OK");

    // Verify response body
    final var foundBody = found.body();
    assertNotNull(foundBody, "Response body should not be null");
    assertEquals(1, foundBody.getContent().size(), "Content size should be 1");

    final var queryUserResponse = (QueryUserResponse) foundBody.getContent().getFirst();
    // Keycloak username is not case-sensitive.
    assertEquals(username.toLowerCase(), queryUserResponse.username(),
        "Username should be " + username);
  }

  @Test
  void query_ShouldReturnFound_WithoutQ(TestInfo testInfo) {

    // Setup
    final var password = "Aa123456789.";
    IntStream.range(0, 20).forEach(i -> {
      final var username = helper.username(testInfo) + "_" + i;
      fluentTestsHelper.createTestUser(username, password);
    });

    // Execute
    final var found = client.toBlocking()
        .exchange(GET("identity/user"), Argument.of(Page.class, QueryUserResponse.class));

    // Verify response status
    assertNotNull(found, "Response should not be null");
    assertEquals(HttpStatus.OK, found.getStatus(), "Status should be OK");

    // Verify response body
    final var foundBody = found.body();
    assertNotNull(foundBody, "Response body should not be null");
    assertTrue(20 <= foundBody.getTotalSize(), "Total size should be greater or equal than 20");
    assertTrue(
        pageableConfiguration.getDefaultPageSize() >= foundBody.getContent().size(),
        "Content size should be less or equal than default page size");
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
