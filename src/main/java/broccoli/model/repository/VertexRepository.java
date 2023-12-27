package broccoli.model.repository;

import broccoli.model.entity.Vertex;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

/**
 * The {@link VertexRepository} class.
 */
@Repository
public interface VertexRepository extends JpaRepository<Vertex, String> {
}
