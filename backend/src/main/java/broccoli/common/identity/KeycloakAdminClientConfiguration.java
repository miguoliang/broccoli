package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link KeycloakAdminClientConfiguration} class.
 */
@ConfigurationProperties("keycloak.admin-client")
public record KeycloakAdminClientConfiguration(
    @NotBlank String serverUrl,
    @NotBlank String realm,
    @NotBlank String clientId,
    @NotBlank String clientSecret,
    @NotBlank String username,
    @NotBlank String password
) {
}
