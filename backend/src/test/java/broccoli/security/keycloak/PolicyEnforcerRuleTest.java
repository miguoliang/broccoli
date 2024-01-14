package broccoli.security.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.IdentityTestHelper;
import broccoli.common.KeycloakClientFacade;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.test.FluentTestsHelper;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PolicyEnforcerRuleTest extends BaseDatabaseTest implements TestPropertyProvider {

  static final String IMAGE_NAME = "quay.io/keycloak/keycloak:23.0.0";

  static final String TEST_REALM = "quickstart";

  @Container
  static KeycloakContainer KEYCLOAK_CONTAINER = new KeycloakContainer(IMAGE_NAME)
      .withRealmImportFile("realm-quickstart.json")
      .withContextPath("/auth")
      .withReuse(true);

  static {
    KEYCLOAK_CONTAINER.start();
  }

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  IdentityTestHelper identityTestHelper;

  @Inject
  Keycloak keycloak;

  KeycloakClientFacade keycloakClientFacade;

  FluentTestsHelper fluentTestsHelper;

  @BeforeAll
  void beforeAll() {

    identityTestHelper.roleId("user");
    identityTestHelper.roleId("user_premium");

    fluentTestsHelper = new FluentTestsHelper(
        KEYCLOAK_CONTAINER.getAuthServerUrl(),
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_CLIENT,
        TEST_REALM
    );
    fluentTestsHelper.init();

    keycloakClientFacade = new KeycloakClientFacade(
        KEYCLOAK_CONTAINER.getAuthServerUrl(),
        TEST_REALM,
        "authz-servlet",
        "secret");
  }

  @BeforeEach
  void setup(TestInfo testInfo) {
    fluentTestsHelper.createTestUser(testInfo.getDisplayName(), "password");
  }

  @Override
  public @NonNull Map<String, String> getProperties() {

    final var jwksUri = String.format("http://%s",
        KEYCLOAK_CONTAINER.getHost() + ":" + KEYCLOAK_CONTAINER.getFirstMappedPort()
            + "/auth/realms/" + TEST_REALM + "/protocol/openid-connect/certs");

    return Map.of(
        "micronaut.security.enabled", "true",
        "micronaut.security.token.jwt.signatures.jwks.default.url", jwksUri,
        "keycloak.admin-client.server-url", KEYCLOAK_CONTAINER.getAuthServerUrl(),
        "keycloak.admin-client.client-secret", DEFAULT_ADMIN_PASSWORD
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
        keycloakClientFacade.getAccessTokenString(testInfo.getDisplayName(), "password");
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
