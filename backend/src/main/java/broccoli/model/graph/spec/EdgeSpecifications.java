package broccoli.model.graph.spec;

import broccoli.model.graph.entity.Edge;
import broccoli.model.graph.entity.Edge_;
import broccoli.model.graph.entity.Vertex_;
import io.micronaut.data.jpa.repository.criteria.Specification;
import java.util.Set;

/**
 * The {@link EdgeSpecifications} class.
 */
public class EdgeSpecifications {

  private EdgeSpecifications() {
  }

  /**
   * Returns a {@link Specification} that matches {@link Edge}s associated with the given.
   *
   * @param vertices vertices
   * @return {@link Specification}
   */
  public static Specification<Edge> associatedWithVertices(Set<String> vertices) {
    return (root, query, builder) -> builder.or(
        root.get(Edge_.IN_VERTEX).get(Vertex_.ID).in(vertices),
        root.get(Edge_.OUT_VERTEX).get(Vertex_.ID).in(vertices));
  }

  /**
   * Returns a {@link Specification} that matches {@link Edge}s with the given names.
   *
   * @param names names
   * @return {@link Specification}
   */
  public static Specification<Edge> nameIn(Set<String> names) {
    return (root, query, builder) -> root.get(Edge_.NAME).in(names);
  }

  /**
   * Returns a {@link Specification} that matches {@link Edge}s with the given types.
   *
   * @param scopes scopes
   * @return {@link Specification}
   */
  public static Specification<Edge> scopeIn(Set<String> scopes) {
    return (root, query, builder) -> root.get(Edge_.SCOPE).in(scopes);
  }
}