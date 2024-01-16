package broccoli.model.graph.restful.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;

/**
 * The {@link DeleteVertexRequest} class.
 */
@Introspected
public record QueryVertexRequest(HttpRequest<?> request, @QueryValue @Nullable String q,
                                 Pageable pageable) {
}
