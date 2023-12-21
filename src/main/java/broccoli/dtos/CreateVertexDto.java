package broccoli.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link broccoli.models.Vertex}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateVertexDto(@NotBlank String name, @NotBlank String type) implements Serializable {
}