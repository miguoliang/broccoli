package broccoli.model.graph.spec;

import broccoli.model.graph.entity.Vertex;
import io.micronaut.data.jpa.repository.criteria.Specification;

/**
 * The {@link VertexSpecifications} class.
 */
public class VertexSpecifications {

  private VertexSpecifications() {
  }

  /**
   * Find vertices by name.
   *
   * @param q Vertex name
   * @return Specification
   */
  public static Specification<Vertex> nameLike(String q) {
    return (root, query, builder) -> builder.like(root.get("name"), "%" + q + "%");
  }
}
