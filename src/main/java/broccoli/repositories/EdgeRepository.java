package broccoli.repositories;

import broccoli.models.Edge;
import broccoli.models.EdgeId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;

@Repository
public interface EdgeRepository extends ReactorPageableRepository<Edge, EdgeId> {
}
