package broccoli.model.identity.http.response;

import broccoli.model.graph.entity.Vertex;

/**
 * The {@link QueryVertexResponse} class.
 */
public record QueryVertexResponse(String id, String name, String type) {

  public static QueryVertexResponse of(Vertex vertex) {
    return new QueryVertexResponse(vertex.getId(), vertex.getName(), vertex.getType());
  }
}
