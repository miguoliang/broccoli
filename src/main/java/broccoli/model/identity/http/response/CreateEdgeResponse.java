package broccoli.model.identity.http.response;

import broccoli.model.graph.entity.Edge;

/**
 * The {@link CreateEdgeResponse} class.
 */
public record CreateEdgeResponse(String inVertexId, String inVertexName, String inVertexType,
                                 String outVertexId, String outVertexName, String outVertexType,
                                 String name, String scope) {

  /**
   * The {@link CreateEdgeResponse} of.
   *
   * @param edge edge
   * @return {@link CreateEdgeResponse}
   */
  public static CreateEdgeResponse of(Edge edge) {
    final var inVertex = edge.getInVertex();
    final var outVertex = edge.getOutVertex();
    return new CreateEdgeResponse(inVertex.getId(), inVertex.getName(), inVertex.getType(),
        outVertex.getId(), outVertex.getName(), outVertex.getType(), edge.getName(),
        edge.getScope());
  }
}
