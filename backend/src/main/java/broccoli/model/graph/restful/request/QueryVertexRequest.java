package broccoli.model.graph.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link DeleteVertexRequest} class.
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record QueryVertexRequest(HttpRequest<?> request, @QueryValue @Nullable String q,
                                 @Nullable Pageable pageable) {
}
