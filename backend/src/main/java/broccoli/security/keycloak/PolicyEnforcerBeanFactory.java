package broccoli.security.keycloak;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.keycloak.adapters.authorization.PolicyEnforcer;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;

/**
 * The {@link PolicyEnforcerBeanFactory} class.
 */
@Requires(env = "dev")
@Factory
public class PolicyEnforcerBeanFactory {

  /**
   * The {@link PolicyEnforcerConfig} bean.
   *
   * @return The {@link PolicyEnforcerConfig} bean.
   */
  @Singleton
  @SneakyThrows
  public PolicyEnforcerConfig policyEnforcerConfig() {
    return JsonSerialization.readValue(getClass()
        .getResourceAsStream("/policy-enforcer.json"), PolicyEnforcerConfig.class);
  }

  /**
   * The {@link PolicyEnforcer} bean.
   *
   * @param enforcerConfig The {@link PolicyEnforcerConfig} bean.
   * @return The {@link PolicyEnforcer} bean.
   */
  @Singleton
  @SneakyThrows
  public PolicyEnforcer policyEnforcer(PolicyEnforcerConfig enforcerConfig) {

    String authServerUrl = enforcerConfig.getAuthServerUrl();

    return PolicyEnforcer.builder()
      .authServerUrl(authServerUrl)
      .realm(enforcerConfig.getRealm())
      .clientId(enforcerConfig.getResource())
      .credentials(enforcerConfig.getCredentials())
      .bearerOnly(false)
      .enforcerConfig(enforcerConfig).build();
  }
}
