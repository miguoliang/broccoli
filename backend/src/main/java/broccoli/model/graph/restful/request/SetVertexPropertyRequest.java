package broccoli.model.graph.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The {@link SetVertexPropertyRequest} class.
 */
@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record SetVertexPropertyRequest(@NotBlank String scope, @NotBlank String key,
                                       @NotNull String value) {
}
