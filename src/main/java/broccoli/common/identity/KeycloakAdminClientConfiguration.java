package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link KeycloakAdminClientConfiguration} class.
 */
@ConfigurationProperties("keycloak.admin-client")
@Getter
@Setter
public class KeycloakAdminClientConfiguration {

  private String serverUrl = "http://localhost:8080/auth";
  private String realm = "master";
  private String clientId = "admin-cli";
  private String clientSecret = "secret";
  private String username = "admin";
  private String password;
}
