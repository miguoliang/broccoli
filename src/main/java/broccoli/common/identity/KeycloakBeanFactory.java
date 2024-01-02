package broccoli.common.identity;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.keycloak.admin.client.Keycloak;

/**
 * The {@link KeycloakBeanFactory} class.
 */
@Factory
public class KeycloakBeanFactory {

  /**
   * The {@link Keycloak} bean.
   *
   * @param keycloakAdminClientConfiguration the {@link KeycloakAdminClientConfiguration} instance.
   * @return the {@link Keycloak} instance.
   */
  @Singleton
  public Keycloak keycloak(KeycloakAdminClientConfiguration keycloakAdminClientConfiguration) {
    return Keycloak.getInstance(
        keycloakAdminClientConfiguration.serverUrl(),
        keycloakAdminClientConfiguration.realm(),
        keycloakAdminClientConfiguration.username(),
        keycloakAdminClientConfiguration.password(),
        keycloakAdminClientConfiguration.clientId(),
        keycloakAdminClientConfiguration.clientSecret()
    );
  }
}
