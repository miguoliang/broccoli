package broccoli.model.identity.http.request;

import broccoli.model.graph.entity.Edge;
import broccoli.model.graph.entity.EdgeId;
import broccoli.model.graph.entity.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The {@link CreateEdgeRequest} class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateEdgeRequest(@NotBlank String inVertexId, @NotBlank String outVertexId,
                                @NotBlank String name, @NotBlank String scope) {

  /**
   * Convert to {@link EdgeId}.
   *
   * @return {@link EdgeId}
   */
  public EdgeId toEdgeId() {
    final var edgeId = new EdgeId();
    edgeId.setInVertex(inVertexId);
    edgeId.setOutVertex(outVertexId);
    edgeId.setName(name);
    edgeId.setScope(scope);
    return edgeId;
  }

  /**
   * Convert to {@link Edge}.
   *
   * @param inVertex  incoming vertex
   * @param outVertex outgoing vertex
   * @return {@link Edge}
   */
  public Edge toEntity(@NotNull Vertex inVertex, @NotNull Vertex outVertex) {

    if (inVertex.getId().equals(inVertexId)) {
      throw new IllegalArgumentException("inVertexId not match");
    }

    if (outVertex.getId().equals(outVertexId)) {
      throw new IllegalArgumentException("outVertexId not match");
    }

    final var edge = new Edge();
    edge.setInVertex(inVertex);
    edge.setOutVertex(outVertex);
    edge.setName(name);
    edge.setScope(scope);
    return edge;
  }
}
