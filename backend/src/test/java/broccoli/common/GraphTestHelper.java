package broccoli.common;

import broccoli.model.graph.entity.Edge;
import broccoli.model.graph.entity.EdgeId;
import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.repository.EdgeRepository;
import broccoli.model.graph.repository.VertexRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

/**
 * The {@link GraphTestHelper} class.
 */
@Singleton
public class GraphTestHelper {

  private final VertexRepository vertexRepository;

  private final EdgeRepository edgeRepository;

  @Inject
  public GraphTestHelper(VertexRepository vertexRepository, EdgeRepository edgeRepository) {
    this.vertexRepository = vertexRepository;
    this.edgeRepository = edgeRepository;
  }

  /**
   * Create a vertex.
   *
   * @param name Vertex name
   * @param type Vertex type
   */
  public Vertex createVertex(String name, String type) {
    final var vertex = new Vertex();
    vertex.setName(name);
    vertex.setType(type);
    return vertexRepository.saveAndFlush(vertex);
  }

  public boolean vertexExists(String name, String type) {
    return vertexRepository.existsById(Vertex.getId(name, type));
  }

  /**
   * If an edge exists.
   *
   * @param inVertexId  in-coming vertex id
   * @param outVertexId out-coming vertex id
   * @param name        edge name
   * @param scope       edge scope
   */
  public boolean edgeExists(String inVertexId, String outVertexId, String name, String scope) {
    final var inVertex = vertexRepository.findById(inVertexId).orElseThrow();
    final var outVertex = vertexRepository.findById(outVertexId).orElseThrow();
    final var edgeId = new EdgeId();
    edgeId.setInVertex(inVertex);
    edgeId.setOutVertex(outVertex);
    edgeId.setName(name);
    edgeId.setScope(scope);
    return edgeRepository.existsById(edgeId);
  }

  /**
   * Create an edge.
   *
   * @param inVertexId  in-coming vertex id
   * @param outVertexId out-coming vertex id
   * @param name        edge name
   * @param scope       edge scope
   */
  @Transactional
  public void createEdge(String inVertexId, String outVertexId, String name, String scope) {
    final var inVertex = vertexRepository.findById(inVertexId).orElseThrow();
    final var outVertex = vertexRepository.findById(outVertexId).orElseThrow();
    final var edge = new Edge();
    edge.setInVertex(inVertex);
    edge.setOutVertex(outVertex);
    edge.setName(name);
    edge.setScope(scope);
    edgeRepository.saveAndFlush(edge);
  }
}
