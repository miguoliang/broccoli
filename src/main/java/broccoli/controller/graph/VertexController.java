package broccoli.controller.graph;

import static broccoli.common.HttpStatusExceptions.conflict;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.request.DeleteVertexRequest;
import broccoli.model.graph.http.request.GetVertexRequest;
import broccoli.model.graph.http.request.QueryVertexRequest;
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
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.RequestBean;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
   * @param getVertexRequest {@link GetVertexRequest}
   * @return {@link GetVertexResponse}
   */
  @Get("/{id}")
  public GetVertexResponse findById(@Valid @RequestBean GetVertexRequest getVertexRequest) {
    return GetVertexResponse.of(vertexRepository.findById(getVertexRequest.id())
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
  public CreateVertexResponse create(@Valid @Body CreateVertexRequest createVertexRequest) {
    final var vertex = createVertexRequest.toEntity();
    if (vertexRepository.existsById(vertex.getId())) {
      throw conflict();
    }
    return CreateVertexResponse.of(vertexRepository.save(createVertexRequest.toEntity()));
  }

  /**
   * Delete a vertex by id.
   *
   * @param deleteVertexRequest {@link DeleteVertexRequest}
   */
  @Delete("/{id}")
  @Status(HttpStatus.NO_CONTENT)
  public void delete(@Valid @RequestBean DeleteVertexRequest deleteVertexRequest) {
    vertexRepository.deleteById(deleteVertexRequest.id());
  }


  /**
   * Query vertices.
   *
   * @param queryVertexRequest {@link QueryVertexRequest}
   * @return {@link Page} of {@link QueryVertexResponse}
   */
  @Get("/{?q}")
  @Transactional
  public Page<QueryVertexResponse> query(
      @Valid @RequestBean QueryVertexRequest queryVertexRequest) {

    if (StringUtils.isBlank(queryVertexRequest.q())) {
      final var pageable = queryVertexRequest.pageable() == null ? Pageable.from(0) :
          queryVertexRequest.pageable();
      return vertexRepository.findAll(pageable).map(QueryVertexResponse::of);
    }

    final var specs = VertexSpecifications.nameLike(queryVertexRequest.q());
    return vertexRepository.findAll(specs, queryVertexRequest.pageable())
        .map(QueryVertexResponse::of);
  }
}
