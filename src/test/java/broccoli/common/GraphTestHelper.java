package broccoli.common;

import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.repository.VertexRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * The {@link GraphTestHelper} class.
 */
@Singleton
public class GraphTestHelper {

  private final VertexRepository vertexRepository;

  @Inject
  public GraphTestHelper(VertexRepository vertexRepository) {
    this.vertexRepository = vertexRepository;
  }

  /**
   * Create a vertex.
   *
   * @param name Vertex name
   * @param type Vertex type
   */
  public void createVertex(String name, String type) {
    final var vertex = new Vertex();
    vertex.setName(name);
    vertex.setType(type);
    vertexRepository.saveAndFlush(vertex);
  }

  public boolean vertexExists(String name, String type) {
    return vertexRepository.existsById(Vertex.getId(name, type));
  }
}
