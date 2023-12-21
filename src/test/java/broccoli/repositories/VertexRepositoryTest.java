package broccoli.repositories;

import broccoli.models.Vertex;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class VertexRepositoryTest {

    @Inject
    VertexRepository vertexRepository;

    @Test
    void saveVertex() {
        final var vertex = new Vertex();
        vertex.setName("test");
        vertex.setType("test");
        vertexRepository.save(vertex).block();

        final var vertexOptional = vertexRepository.findById(vertex.getId()).blockOptional();
        assertTrue(vertexOptional.isPresent());
    }
}