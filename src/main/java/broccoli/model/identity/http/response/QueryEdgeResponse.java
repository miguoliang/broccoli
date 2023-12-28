package broccoli.model.identity.http.response;

import broccoli.model.graph.entity.Edge;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The {@link QueryEdgeResponse} class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record QueryEdgeResponse(String inVertexId, String inVertexName, String inVertexType,
                                String outVertexId, String outVertexName, String outVertexType,
                                String name, String scope) {
  /**
   * Convert to {@link QueryEdgeResponse}.
   *
   * @param edge edge
   * @return {@link QueryEdgeResponse}
   */
  public static QueryEdgeResponse of(Edge edge) {
    final var inVertex = edge.getInVertex();
    final var outVertex = edge.getOutVertex();
    return new QueryEdgeResponse(inVertex.getId(), inVertex.getName(), inVertex.getType(),
        outVertex.getId(), outVertex.getName(), outVertex.getType(), edge.getName(),
        edge.getScope());
  }
}
