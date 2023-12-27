package broccoli.security.keycloak;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.rules.SecurityRuleResult;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.authorization.PolicyEnforcer;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Requires(classes = {PolicyEnforcer.class, HttpRequest.class})
@Singleton
@Slf4j
public class PolicyEnforcerRule implements SecurityRule<HttpRequest<?>> {

  private final PolicyEnforcer policyEnforcer;

  public PolicyEnforcerRule(PolicyEnforcer policyEnforcer) {
    this.policyEnforcer = policyEnforcer;
  }

  @Override
  public Publisher<SecurityRuleResult> check(@Nullable HttpRequest<?> request, @Nullable Authentication authentication) {
    if (authentication == null) {
      return Mono.just(SecurityRuleResult.REJECTED);
    }
    final var authzContext = policyEnforcer.enforce(new SpiHttpRequest(request), new SpiHttpResponse());
    return Mono.just(authzContext.isGranted() ? SecurityRuleResult.ALLOWED : SecurityRuleResult.REJECTED);
  }
}
