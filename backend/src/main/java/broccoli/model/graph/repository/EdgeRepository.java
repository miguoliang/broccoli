package broccoli.model.graph.repository;

import broccoli.model.graph.entity.Edge;
import broccoli.model.graph.entity.EdgeId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@link EdgeRepository} class.
 */
@Repository
public interface EdgeRepository
    extends JpaRepository<Edge, EdgeId>, JpaSpecificationExecutor<Edge> {
}
