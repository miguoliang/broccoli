package broccoli.model.graph.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link DeleteVertexRequest} class.
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteVertexRequest(HttpRequest<?> request, @PathVariable @NotBlank String id) {
}
