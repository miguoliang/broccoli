package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link KeycloakAdminClientConfiguration} class.
 */
@ConfigurationProperties("keycloak-admin-client")
@Getter
@Setter
public class KeycloakAdminClientConfiguration {

  private String serverUrl;
  private String realm;
  private String clientId;
  private String clientSecret;
  private String username;
  private String password;
}
