package broccoli.model.graph.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link GetVertexRequest} class.
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetVertexRequest(HttpRequest<?> request, @PathVariable @NotBlank String id) {
}
