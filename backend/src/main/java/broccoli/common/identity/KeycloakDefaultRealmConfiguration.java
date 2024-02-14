package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link KeycloakDefaultRealmConfiguration} class.
 */
@Requires(env = "dev")
@ConfigurationProperties("keycloak.default")
public record KeycloakDefaultRealmConfiguration(@NotBlank String realm) {
}
