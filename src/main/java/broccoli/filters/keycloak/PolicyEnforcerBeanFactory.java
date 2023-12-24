package broccoli.filters.keycloak;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.keycloak.adapters.authorization.PolicyEnforcer;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;

@Factory
public class PolicyEnforcerBeanFactory {

    @Singleton
    @SneakyThrows
    public PolicyEnforcerConfig policyEnforcerConfig() {
        return JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer.json"), PolicyEnforcerConfig.class);
    }

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
