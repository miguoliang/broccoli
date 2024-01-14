package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link KeycloakAdminClientConfiguration} class.
 */
@AllArgsConstructor
@Getter
@Setter
@ConfigurationProperties("keycloak.admin-client")
public class KeycloakAdminClientConfiguration {
  private @NotBlank String serverUrl;
  private @NotBlank String realm;
  private @NotBlank String clientId;
  private @NotBlank String clientSecret;
  private @NotBlank String username;
  private @NotBlank String password;
}
