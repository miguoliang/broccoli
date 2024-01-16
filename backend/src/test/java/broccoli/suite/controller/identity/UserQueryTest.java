package broccoli.suite.controller.identity;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.model.identity.restful.response.SearchUsersResponse;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Inject;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The {@link UserQueryTest} class.
 */
class UserQueryTest extends GeneralTestSetup {

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  @Test
  void query_ShouldReturnFound_WithQ(TestInfo testInfo) {

    // Setup
    final var username = identityTestHelper.username(testInfo);
    final var password = "Aa123456789.";
    fluentTestsHelper.createTestUser(username, password);

    // Execute
    final var found = client.toBlocking().exchange(GET("api/identity/user?q=" + username),
        Argument.of(Page.class, SearchUsersResponse.class));

    // Verify response status
    assertNotNull(found, "Response should not be null");
    assertEquals(HttpStatus.OK, found.getStatus(), "Status should be OK");

    // Verify response body
    final var foundBody = found.body();
    assertNotNull(foundBody, "Response body should not be null");
    assertEquals(1, foundBody.getContent().size(), "Content size should be 1");

    final var queryUserResponse = (SearchUsersResponse) foundBody.getContent().getFirst();
    // Keycloak username is not case-sensitive.
    assertEquals(username.toLowerCase(), queryUserResponse.username(),
        "Username should be " + username);
  }

  @Test
  void query_ShouldReturnFound_WithoutQ(TestInfo testInfo) {

    // Setup
    final var password = "Aa123456789.";
    IntStream.range(0, 20).forEach(i -> {
      final var username = identityTestHelper.username(testInfo) + "_" + i;
      fluentTestsHelper.createTestUser(username, password);
    });

    // Execute
    final var found = client.toBlocking()
        .exchange(GET("api/identity/user"), Argument.of(Page.class, SearchUsersResponse.class));

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
}
