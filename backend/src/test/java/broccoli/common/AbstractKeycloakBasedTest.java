package broccoli.common;

import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_TEST_REALM;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import broccoli.common.identity.KeycloakAdminClientConfiguration;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.micronaut.test.annotation.MockBean;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.test.FluentTestsHelper;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * The {@link AbstractKeycloakBasedTest} class.
 * <p>See also:
 * <a href="https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers:~:text=stop()%20afterwards%0A%7D-,Singleton%20containers,-Sometimes%20it%20might">Singleton containers</a></p>
 */
public abstract class AbstractKeycloakBasedTest {

  static final String IMAGE_NAME = "quay.io/keycloak/keycloak:23.0.0";
  public static final KeycloakContainer KEYCLOAK_CONTAINER;

  public static final GenericContainer<?> MAILHOG_CONTAINER;

  public static final Keycloak KEYCLOAK_ADMIN_CLIENT;

  static {

    final var network = Network.newNetwork();

    MAILHOG_CONTAINER = new GenericContainer<>("mailhog/mailhog")
        .withNetwork(network)
        .withNetworkAliases("mailhog")
        .waitingFor(Wait.forHttp("/"));

    KEYCLOAK_CONTAINER = new KeycloakContainer(IMAGE_NAME)
        .withRealmImportFile("realm-quickstart.json")
        .withContextPath("/auth")
        .withNetwork(network)
        .withNetworkAliases("keycloak");

    KEYCLOAK_CONTAINER.start();
    MAILHOG_CONTAINER.start();

    KEYCLOAK_ADMIN_CLIENT = Keycloak.getInstance(
        KEYCLOAK_CONTAINER.getAuthServerUrl(),
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_CLIENT);
  }

  protected FluentTestsHelper fluentTestsHelper;

  protected String testRealm;

  protected AbstractKeycloakBasedTest() {
    this(DEFAULT_TEST_REALM);
  }

  /**
   * Instantiates a new {@link AbstractKeycloakBasedTest}.
   */
  protected AbstractKeycloakBasedTest(String testRealm) {
    fluentTestsHelper = new FluentTestsHelper(
        KEYCLOAK_CONTAINER.getAuthServerUrl(),
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_CLIENT,
        testRealm
    );
    fluentTestsHelper.init();
    this.testRealm = testRealm;
  }

  @MockBean(KeycloakAdminClientConfiguration.class)
  protected KeycloakAdminClientConfiguration keycloakAdminClientConfiguration() {
    final var configurationMock = mock(KeycloakAdminClientConfiguration.class);
    when(configurationMock.getServerUrl()).thenReturn(KEYCLOAK_CONTAINER.getAuthServerUrl());
    when(configurationMock.getRealm()).thenReturn(testRealm);
    when(configurationMock.getClientId()).thenReturn(DEFAULT_ADMIN_CLIENT);
    when(configurationMock.getClientSecret()).thenReturn(DEFAULT_ADMIN_PASSWORD);
    when(configurationMock.getUsername()).thenReturn(DEFAULT_ADMIN_USERNAME);
    when(configurationMock.getPassword()).thenReturn(DEFAULT_ADMIN_PASSWORD);
    return configurationMock;
  }
}
