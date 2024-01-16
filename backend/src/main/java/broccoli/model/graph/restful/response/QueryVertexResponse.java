package broccoli.model.graph.restful.response;

import broccoli.model.graph.entity.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link QueryVertexResponse} class.
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record QueryVertexResponse(String id, String name, String type) {

  public static QueryVertexResponse of(Vertex vertex) {
    return new QueryVertexResponse(vertex.getId(), vertex.getName(), vertex.getType());
  }
}
