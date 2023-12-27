package broccoli.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link broccoli.models.Vertex} entity.
 */
@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateVertexDto(@NotBlank String name, @NotBlank String type) {
}