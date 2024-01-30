package broccoli.model.graph.repository;

import broccoli.model.graph.entity.VertexProperty;
import broccoli.model.graph.entity.VertexPropertyId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@link VertexPropertyRepository} class.
 */
@Repository
public interface VertexPropertyRepository
    extends JpaRepository<VertexProperty, VertexPropertyId>,
    JpaSpecificationExecutor<VertexProperty> {
}
