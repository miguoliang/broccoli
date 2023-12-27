package broccoli.security.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;

import broccoli.commons.KeycloakClientFacade;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.test.FluentTestsHelper;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@MicronautTest
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PolicyEnforcerRuleTest implements TestPropertyProvider {

  @Container
  static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:23.0.0")
      .withRealmImportFile("realm-export.json")
      .withContextPath("/auth")
      .withReuse(true);
  @Inject
  @Client("/")
  HttpClient client;
  FluentTestsHelper fluentTestsHelper;
  KeycloakClientFacade keycloakClientFacade;

  static String getJwksUri() {
    return String.format("http://%s", keycloak.getHost() + ":" + keycloak.getFirstMappedPort() +
        "/auth/realms/quickstart/protocol/openid-connect/certs");
  }

  @BeforeAll
  public void setup() {
    fluentTestsHelper = new FluentTestsHelper(
        keycloak.getAuthServerUrl(),
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_CLIENT,
        "quickstart"
    );
    fluentTestsHelper.init();

    keycloakClientFacade = new KeycloakClientFacade(
        keycloak.getAuthServerUrl(),
        "quickstart",
        "authz-servlet",
        "secret"
    );
  }

  @Override
  public @NonNull Map<String, String> getProperties() {
    if (!keycloak.isRunning()) {
      keycloak.start();
    }
    return Map.of(
        "micronaut.security.token.jwt.signatures.jwks.default.url", getJwksUri()
    );
  }

  @Test
  void testSecurity_ShouldBeUnauthorizedWhenTokenNotFound() {

    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking().exchange(HttpRequest.GET("/vertex").accept("text/plain")),
        "No access token was supplied to the request");
    assertEquals(401, thrown.getStatus().getCode());
  }

  @Test
  void testSecurity_ShouldBeForbiddenWhenTokenIsInvalid() {

    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(HttpRequest.GET("/vertex").accept("text/plain").bearerAuth("invalid")),
        "Access token is invalid");
    assertEquals(401, thrown.getStatus().getCode());
  }

  @Test
  void testSecurity_ShouldBeForbiddenWhenBeRejectedByKeycloak(TestInfo testInfo) {

    fluentTestsHelper.assignRoleWithUser(testInfo.getDisplayName(), "user");
    final var accessToken =
        keycloakClientFacade.getAccessTokenString(testInfo.getDisplayName(), "password");
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(HttpRequest.GET("/edge").accept("text/plain").bearerAuth(accessToken)),
        "Access token does not have expected scope");
    assertEquals(403, thrown.getStatus().getCode());
  }

  @Test
  void testSecurity_ShouldBeAllowedWhenAnonymousIsAccepted() {

    final var response =
        client.toBlocking().exchange(HttpRequest.GET("/vertex/anonymous").accept("text/plain"));
    assertEquals(200, response.getStatus().getCode());
  }

  @Test
  void testSecurity_ShouldBePermittedWhenBeAllowedByKeycloak(TestInfo testInfo) {

    fluentTestsHelper.assignRoleWithUser(testInfo.getDisplayName(), "user");
    final var accessToken =
        keycloakClientFacade.getAccessTokenString(testInfo.getDisplayName(), "password");
    final var response = client.toBlocking()
        .exchange(HttpRequest.GET("/vertex").accept("text/plain").bearerAuth(accessToken));
    assertEquals(200, response.getStatus().getCode());
  }

  @BeforeEach
  void setup(TestInfo testInfo) {
    fluentTestsHelper.createTestUser(testInfo.getDisplayName(), "password");
  }

  @AfterEach
  void cleanup(TestInfo testInfo) {
    fluentTestsHelper.deleteTestUser(testInfo.getDisplayName());
  }

  @MockBean(PolicyEnforcerConfig.class)
  PolicyEnforcerConfig policyEnforcerConfig() {

    final var enforcerConfig = new PolicyEnforcerConfig();
    enforcerConfig.setAuthServerUrl(keycloak.getAuthServerUrl());
    enforcerConfig.setRealm("quickstart");
    enforcerConfig.setResource("authz-servlet");
    enforcerConfig.setCredentials(Map.of("secret", "secret"));
    return enforcerConfig;
  }
}
