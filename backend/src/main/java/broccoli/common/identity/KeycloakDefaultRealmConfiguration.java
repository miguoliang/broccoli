package broccoli.common.identity;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link KeycloakDefaultRealmConfiguration} class.
 */
@ConfigurationProperties("keycloak.default")
public record KeycloakDefaultRealmConfiguration(@NotBlank String realm) {
}
