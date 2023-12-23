package broccoli.controllers;

import broccoli.models.Vertex;
import broccoli.repositories.VertexRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

@Controller("/vertex")
public class VertexController {

    private final VertexRepository vertexRepository;

    @Inject
    public VertexController(VertexRepository vertexRepository) {
        this.vertexRepository = vertexRepository;
    }

    @Secured("isAuthenticated()")
    @Get("/{id}")
    public Publisher<Vertex> findById(@PathVariable String id) {
        return vertexRepository.findById(id);
    }
}
