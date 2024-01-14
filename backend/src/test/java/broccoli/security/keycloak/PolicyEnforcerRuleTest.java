package broccoli.security.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.common.BaseKeycloakTest;
import broccoli.common.KeycloakClientFacade;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.slf4j.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PolicyEnforcerRuleTest extends BaseKeycloakTest implements TestPropertyProvider {

  private static final Logger LOGGER =
      org.slf4j.LoggerFactory.getLogger(PolicyEnforcerRuleTest.class);

  private static final String TEST_REALM = "quickstart";

  @Inject
  @Client("/")
  HttpClient client;

  private static final KeycloakClientFacade KEYCLOAK_CLIENT_FACADE = new KeycloakClientFacade(
      KEYCLOAK_CONTAINER.getAuthServerUrl(),
      TEST_REALM,
      "authz-servlet",
      "secret"
  );

  PolicyEnforcerRuleTest() {
    super(TEST_REALM);
  }

  @BeforeEach
  void setup(TestInfo testInfo) {
    fluentTestsHelper.createTestUser(testInfo.getDisplayName(), "password");
  }

  @AfterAll
  void afterAll() {
    LOGGER.error("Stopping Keycloak and Mailhog containers");
    KEYCLOAK_CONTAINER.stop();
    MAILHOG_CONTAINER.stop();
    LOGGER.error("Keycloak and Mailhog containers stopped");
  }

  private String getJwksUri() {
    return String.format("http://%s",
        KEYCLOAK_CONTAINER.getHost() + ":" + KEYCLOAK_CONTAINER.getFirstMappedPort()
            + "/auth/realms/" + TEST_REALM + "/protocol/openid-connect/certs");
  }

  @Override
  public @NonNull Map<String, String> getProperties() {
    return Map.of(
        "micronaut.security.enabled", "true",
        "logger.levels.org.keycloak", "INFO",
        "logger.levels.io.micronaut.security", "INFO",
        "micronaut.security.token.jwt.signatures.jwks.default.url", getJwksUri()
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "invalid"
  })
  void testSecurity_BadToken(String token) {

    final var rawRequest = HttpRequest.GET("/foo/protected").accept("text/plain");
    final var httpRequest = token.isEmpty() ? rawRequest : rawRequest.bearerAuth(token);
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking().exchange(httpRequest),
        "No access token was supplied to the request");
    assertEquals(401, thrown.getStatus().getCode());
  }

  @ParameterizedTest
  @CsvSource({
      "get, /foo/anonymous, 200",
      "get, /foo/protected, 401",
      "post, /foo/protected, 401",
      "get, /foo/protected/premium, 401",
      "get, /foo/protected/premium, 401",
  })
  void testSecurity_AnonymousAccess(String method, String path, Integer expectedStatus) {

    final var httpMethod = HttpMethod.valueOf(method.toUpperCase());
    final var httpRequest = HttpRequest.create(httpMethod, path).accept("text/plain");
    if (expectedStatus == 200) {
      assertEquals(expectedStatus, client.toBlocking().exchange(httpRequest).getStatus().getCode());
    } else {
      final var thrown = assertThrowsExactly(
          HttpClientResponseException.class,
          () -> client.toBlocking().exchange(httpRequest),
          "Access is denied");
      assertEquals(expectedStatus, thrown.getStatus().getCode());
    }
  }

  @ParameterizedTest
  @CsvSource({
      "user, get, /foo/anonymous, 200",
      "user, get, /foo/protected, 200",
      "user, post, /foo/protected, 403",
      "user, get, /foo/protected/premium, 403",
      "user_premium, get, /foo/anonymous, 200",
      "user_premium, get, /foo/protected/premium, 200",
      "user_premium, post, /foo/protected, 201",
  })
  void testSecurity_UserAccess(
      String role, String method, String path, Integer expectedStatus, TestInfo testInfo) {

    fluentTestsHelper.assignRoleWithUser(testInfo.getDisplayName(), role);
    final var accessToken =
        KEYCLOAK_CLIENT_FACADE.getAccessTokenString(testInfo.getDisplayName(), "password");
    final var httpMethod = HttpMethod.valueOf(method.toUpperCase());
    final var httpRequest =
        HttpRequest.create(httpMethod, path).accept("text/plain").bearerAuth(accessToken);
    if (expectedStatus < 400) {
      final var response = client.toBlocking().exchange(httpRequest);
      assertEquals(expectedStatus, response.getStatus().getCode());
    } else {
      final var thrown = assertThrowsExactly(
          HttpClientResponseException.class,
          () -> client.toBlocking().exchange(httpRequest),
          "Access is denied");
      assertEquals(expectedStatus, thrown.getStatus().getCode());
    }
  }

  @AfterEach
  void cleanup(TestInfo testInfo) {
    fluentTestsHelper.deleteTestUser(testInfo.getDisplayName());
  }

  @MockBean(PolicyEnforcerConfig.class)
  PolicyEnforcerConfig policyEnforcerConfig() {

    final var enforcerConfig = new PolicyEnforcerConfig();
    enforcerConfig.setAuthServerUrl(KEYCLOAK_CONTAINER.getAuthServerUrl());
    enforcerConfig.setRealm("quickstart");
    enforcerConfig.setResource("authz-servlet");
    enforcerConfig.setHttpMethodAsScope(true);
    enforcerConfig.setCredentials(Map.of("secret", "secret"));
    return enforcerConfig;
  }
}
