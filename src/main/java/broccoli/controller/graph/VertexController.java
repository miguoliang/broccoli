package broccoli.controller.graph;

import static broccoli.common.HttpStatusExceptions.conflict;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.response.CreateVertexResponse;
import broccoli.model.graph.http.response.GetVertexResponse;
import broccoli.model.graph.repository.VertexRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link VertexController} class.
 */
@Controller("/graph/vertex")
public class VertexController {

  private final VertexRepository vertexRepository;

  @Inject
  public VertexController(VertexRepository vertexRepository) {
    this.vertexRepository = vertexRepository;
  }

  /**
   * Get a vertex by id.
   *
   * @param id Vertex id
   * @return Vertex just found
   */
  @Get("{id}")
  public GetVertexResponse findById(@PathVariable @NotBlank String id) {
    return GetVertexResponse.of(vertexRepository.findById(id)
        .orElseThrow(HttpStatusExceptions::notFound));
  }

  /**
   * Create a vertex.
   *
   * @param createVertexRequest Basic vertex info
   * @return Vertex just created
   */
  @Post
  @Status(HttpStatus.CREATED)
  public CreateVertexResponse create(@Body CreateVertexRequest createVertexRequest) {
    final var vertex = createVertexRequest.toEntity();
    if (vertexRepository.existsById(vertex.getId())) {
      throw conflict();
    }
    return CreateVertexResponse.of(vertexRepository.save(createVertexRequest.toEntity()));
  }

  /**
   * Delete a vertex by id.
   *
   * @param id Vertex id
   */
  @Delete("{id}")
  public void delete(@PathVariable @NotBlank String id) {
    vertexRepository.deleteById(id);
  }
}
