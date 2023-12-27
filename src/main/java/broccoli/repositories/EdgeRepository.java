package broccoli.repositories;

import broccoli.models.Edge;
import broccoli.models.EdgeId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, EdgeId> {
}
