package broccoli.repositories;

import broccoli.models.Vertex;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;

@Repository
public interface VertexRepository extends ReactorPageableRepository<Vertex, String> {
}
