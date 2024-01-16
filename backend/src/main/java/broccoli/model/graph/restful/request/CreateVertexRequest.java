package broccoli.model.graph.restful.request;

import broccoli.model.graph.entity.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link CreateVertexRequest} class.
 *
 * @param name Vertex name
 * @param type Vertex type
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateVertexRequest(@NotBlank String name, @NotBlank String type) {

  /**
   * Convert to {@link Vertex}.
   *
   * @return {@link Vertex}
   */
  public Vertex toEntity() {

    final var vertex = new Vertex();
    vertex.setName(name);
    vertex.setType(type);
    return vertex;
  }
}