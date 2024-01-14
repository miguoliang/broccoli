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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.test.FluentTestsHelper;
import org.slf4j.Logger;
import org.testcontainers.containers.GenericContainer;

/**
 * The {@link BaseKeycloakTest} class.
 * <p>See also:
 * <a href="https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers:~:text=stop()%20afterwards%0A%7D-,Singleton%20containers,-Sometimes%20it%20might">Singleton containers</a></p>
 */
@MicronautTest(transactional = false)
public abstract class BaseKeycloakTest extends BaseDatabaseTest {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BaseKeycloakTest.class);

  static final String IMAGE_NAME = "quay.io/keycloak/keycloak:23.0.0";

  public static KeycloakContainer KEYCLOAK_CONTAINER;

  public static GenericContainer<?> MAILHOG_CONTAINER;

  public static Keycloak KEYCLOAK_ADMIN_CLIENT;

  static {

    LOGGER.error("Starting Keycloak and Mailhog containers");

    MAILHOG_CONTAINER = new GenericContainer<>("mailhog/mailhog")
        .withNetworkAliases("mailhog");

    KEYCLOAK_CONTAINER = new KeycloakContainer(IMAGE_NAME)
        .withRealmImportFile("realm-quickstart.json")
        .withContextPath("/auth")
        .withNetworkAliases("keycloak")
        .dependsOn(MAILHOG_CONTAINER);

    KEYCLOAK_CONTAINER.start();

    KEYCLOAK_ADMIN_CLIENT = Keycloak.getInstance(
        KEYCLOAK_CONTAINER.getAuthServerUrl(),
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_CLIENT);

    final var adminRepresentation =
        KEYCLOAK_ADMIN_CLIENT.realm(DEFAULT_ADMIN_REALM).users().search("admin").getFirst();
    final var adminId = adminRepresentation.getId();
    adminRepresentation.setEmail("admin@example.com");
    KEYCLOAK_ADMIN_CLIENT.realm(DEFAULT_ADMIN_REALM).users().get(adminId)
        .update(adminRepresentation);

    final var representation = KEYCLOAK_ADMIN_CLIENT.realm(DEFAULT_ADMIN_REALM).toRepresentation();
    representation.setEditUsernameAllowed(true);
    representation.setSmtpServer(Map.of(
        "replyToDisplayName", "",
        "starttls", "false",
        "auth", "",
        "port", "1025",
        "replyTo", "",
        "host", "mailhog",
        "from", "from@example.com",
        "fromDisplayName", "",
        "envelopeFrom", "",
        "ssl", "false"
    ));
    KEYCLOAK_ADMIN_CLIENT.realm("master").update(representation);
    LOGGER.error("Keycloak started at {}", KEYCLOAK_CONTAINER.getAuthServerUrl());
  }

  protected FluentTestsHelper fluentTestsHelper;

  protected String testRealm;

  protected BaseKeycloakTest() {
    this(DEFAULT_TEST_REALM);
  }

  /**
   * Instantiates a new {@link BaseKeycloakTest}.
   */
  protected BaseKeycloakTest(String testRealm) {
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
