package broccoli.model.identity.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link SearchUsersRequest} class.
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchUsersRequest(HttpRequest<?> request, @QueryValue @Nullable String q,
                                 Pageable pageable) {
}
