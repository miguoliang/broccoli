package broccoli.filters.keycloak;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.RequestFilter;
import io.micronaut.http.annotation.ServerFilter;
import io.micronaut.http.filter.FilterContinuation;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.authorization.PolicyEnforcer;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;

@ServerFilter("/**")
@Slf4j
public class PolicyEnforcerFilter {

    private final PolicyEnforcer policyEnforcer;

    @SneakyThrows
    public PolicyEnforcerFilter(PolicyEnforcer policyEnforcer) {

        this.policyEnforcer = policyEnforcer;
    }

    @RequestFilter
    @ExecuteOn(TaskExecutors.BLOCKING)
    public void filterRequest(HttpRequest<?> request, FilterContinuation<MutableHttpResponse<?>> continuation) {

        final var httpResponse = continuation.proceed();
        final var authzContext = policyEnforcer.enforce(new SpiHttpRequest(request), new SpiHttpResponse(httpResponse));
        if (authzContext.isGranted()) {
            log.info("Request authorized");
        } else {
            log.info("Unauthorized request to path [{}], aborting the filter chain", request.getUri());
        }
    }
}
