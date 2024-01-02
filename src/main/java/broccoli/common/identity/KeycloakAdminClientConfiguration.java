package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotNull;

/**
 * The {@link KeycloakAdminClientConfiguration} class.
 */
@ConfigurationProperties("keycloak.admin-client")
public record KeycloakAdminClientConfiguration(
    @NotNull String serverUrl,
    @NotNull String realm,
    @NotNull String clientId,
    @NotNull String clientSecret,
    @NotNull String username,
    @NotNull String password) {
}
