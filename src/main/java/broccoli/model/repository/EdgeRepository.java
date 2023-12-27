package broccoli.model.repository;

import broccoli.model.entity.Edge;
import broccoli.model.entity.EdgeId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

/**
 * The {@link EdgeRepository} class.
 */
@Repository
public interface EdgeRepository extends JpaRepository<Edge, EdgeId> {
}
