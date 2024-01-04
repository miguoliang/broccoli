package broccoli.common;

import dasniko.testcontainers.keycloak.KeycloakContainer;

/**
 * The {@link AbstractKeycloakBasedTest} class.
 * <p>See also:
 * <a href="https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers:~:text=stop()%20afterwards%0A%7D-,Singleton%20containers,-Sometimes%20it%20might">Singleton containers</a></p>
 */
public abstract class AbstractKeycloakBasedTest {

  static final String IMAGE_NAME = "quay.io/keycloak/keycloak:23.0.0";
  public static final KeycloakContainer KEYCLOAK_CONTAINER;

  static {

    KEYCLOAK_CONTAINER = new KeycloakContainer(IMAGE_NAME)
        .withRealmImportFile("realm-quickstart.json")
        .withContextPath("/auth");

    KEYCLOAK_CONTAINER.start();
  }
}
