package broccoli.common;

import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;

import dasniko.testcontainers.keycloak.KeycloakContainer;
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

  static {

    final var network = Network.newNetwork();

    MAILHOG_CONTAINER = new GenericContainer<>("mailhog/mailhog")
        .withNetwork(network)
        .withNetworkAliases("mailhog")
        .waitingFor(Wait.forHttp("/"));

    KEYCLOAK_CONTAINER = new KeycloakContainer(IMAGE_NAME)
        .withRealmImportFile("realm-master.json")
        .withRealmImportFile("realm-quickstart.json")
        .withContextPath("/auth")
        .withNetwork(network)
        .withNetworkAliases("keycloak");

    KEYCLOAK_CONTAINER.start();
    MAILHOG_CONTAINER.start();
  }

  protected FluentTestsHelper fluentTestsHelper;

  /**
   * Instantiates a new {@link AbstractKeycloakBasedTest}.
   */
  protected AbstractKeycloakBasedTest() {
    fluentTestsHelper = new FluentTestsHelper(
        KEYCLOAK_CONTAINER.getAuthServerUrl(),
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_CLIENT,
        testRealm()
    );
    fluentTestsHelper.init();
  }

  protected final String getJwksUri() {
    return String.format("http://%s",
        KEYCLOAK_CONTAINER.getHost() + ":" + KEYCLOAK_CONTAINER.getFirstMappedPort()
            + "/auth/realms/" + testRealm() + "/protocol/openid-connect/certs");
  }

  protected String testRealm() {
    return "master";
  }
}
