package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;

/**
 * The {@link KeycloakAdminClientConfiguration} class.
 */
@ConfigurationProperties("keycloak.admin-client")
public record KeycloakAdminClientConfiguration(
    String serverUrl,
    String realm,
    String clientId,
    String clientSecret,
    String username,
    String password) {
}
