package broccoli.model.graph.http.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.PathVariable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link DeleteVertexRequest} class.
 */
@Introspected
public record DeleteVertexRequest(HttpRequest<?> request, @PathVariable @NotBlank String id) {
}
