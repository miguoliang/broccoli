package broccoli.model.identity.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;

/**
 * The {@link QueryUserRequest} class.
 */
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public record QueryUserRequest(HttpRequest<?> request, @QueryValue @Nullable String q,
                               Pageable pageable) {
}
