package broccoli.model.http.response;

import broccoli.model.entity.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link GetVertexResponse} class.
 *
 * @param id   Vertex id
 * @param name Vertex name
 * @param type Vertex type
 */
@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetVertexResponse(String id, String name, String type) {

  public static GetVertexResponse of(Vertex vertex) {
    return new GetVertexResponse(vertex.getId(), vertex.getName(), vertex.getType());
  }
}
