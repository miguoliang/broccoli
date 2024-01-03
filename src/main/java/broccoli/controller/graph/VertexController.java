package broccoli.controller.graph;

import static broccoli.common.HttpStatusExceptions.conflict;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.response.CreateVertexResponse;
import broccoli.model.graph.http.response.GetVertexResponse;
import broccoli.model.graph.http.response.QueryVertexResponse;
import broccoli.model.graph.repository.VertexRepository;
import broccoli.model.graph.spec.VertexSpecifications;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;

/**
 * The {@link VertexController} class.
 */
@Controller("/graph/vertex")
@Validated
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
  public CreateVertexResponse create(@Body @Valid CreateVertexRequest createVertexRequest) {
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
  @Status(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable @NotBlank String id) {
    vertexRepository.deleteById(id);
  }

  /**
   * Query vertices.
   *
   * @param q        query string
   * @param pageable page info
   * @return vertices
   */
  @Get
  public Page<QueryVertexResponse> query(@QueryValue String q, Pageable pageable) {

    if (StringUtils.isBlank(q)) {
      return vertexRepository.findAll(pageable).map(QueryVertexResponse::of);
    }

    final var specs = VertexSpecifications.nameLike(q);
    return vertexRepository.findAll(specs, pageable).map(QueryVertexResponse::of);
  }
}
