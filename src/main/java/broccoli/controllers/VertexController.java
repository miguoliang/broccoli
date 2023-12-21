package broccoli.controllers;

import broccoli.models.Vertex;
import broccoli.repositories.VertexRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

@Controller("/vertex")
public class VertexController {

    @Inject
    private VertexRepository vertexRepository;

    @Get("/{id}")
    public Publisher<Vertex> findById(@PathVariable String id) {
        return vertexRepository.findById(id);
    }
}
