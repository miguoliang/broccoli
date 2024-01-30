package broccoli.endpoint.restful.graph;

import static broccoli.common.HttpStatusExceptions.conflict;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.graph.entity.VertexProperty;
import broccoli.model.graph.repository.VertexPropertyRepository;
import broccoli.model.graph.repository.VertexRepository;
import broccoli.model.graph.restful.request.CreateVertexRequest;
import broccoli.model.graph.restful.request.DeleteVertexRequest;
import broccoli.model.graph.restful.request.GetVertexRequest;
import broccoli.model.graph.restful.request.QueryVertexRequest;
import broccoli.model.graph.restful.request.SetVertexPropertyRequest;
import broccoli.model.graph.restful.response.CreateVertexResponse;
import broccoli.model.graph.restful.response.GetVertexResponse;
import broccoli.model.graph.restful.response.QueryVertexResponse;
import broccoli.model.graph.spec.VertexSpecifications;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.RequestBean;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;

/**
 * The {@link VertexController} class.
 */
@Controller("/api/graph/vertex")
@Validated
public class VertexController {

  private final VertexRepository vertexRepository;

  private final VertexPropertyRepository vertexPropertyRepository;

  @Inject
  public VertexController(VertexRepository vertexRepository,
                          VertexPropertyRepository vertexPropertyRepository) {
    this.vertexRepository = vertexRepository;
    this.vertexPropertyRepository = vertexPropertyRepository;
  }

  /**
   * Get a vertex by id.
   *
   * @param getVertexRequest {@link GetVertexRequest}
   * @return {@link GetVertexResponse}
   */
  @Get("/{id}")
  public GetVertexResponse findVertexById(@Valid @RequestBean GetVertexRequest getVertexRequest) {
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
  public CreateVertexResponse createVertex(@Valid @Body CreateVertexRequest createVertexRequest) {
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
  public void deleteVertex(@Valid @RequestBean DeleteVertexRequest deleteVertexRequest) {
    vertexRepository.deleteById(deleteVertexRequest.id());
  }

  /**
   * Search vertices.
   *
   * @param queryVertexRequest {@link QueryVertexRequest}
   * @return {@link Page} of {@link QueryVertexResponse}
   */
  @Get("/{?q}")
  @Transactional
  public Page<QueryVertexResponse> searchVertices(
      @Valid @RequestBean QueryVertexRequest queryVertexRequest) {

    if (StringUtils.isBlank(queryVertexRequest.q())) {
      return vertexRepository.findAll(queryVertexRequest.pageable()).map(QueryVertexResponse::of);
    }

    final var specs = VertexSpecifications.nameLike(queryVertexRequest.q());
    return vertexRepository.findAll(specs, queryVertexRequest.pageable())
        .map(QueryVertexResponse::of);
  }

  /**
   * Set vertex property.
   *
   * @param setVertexPropertyRequest {@link SetVertexPropertyRequest}
   */
  @Put("/{id}/property")
  @Status(HttpStatus.NO_CONTENT)
  @Transactional
  public void setProperty(@PathVariable @NotBlank String id,
                          @Valid @Body SetVertexPropertyRequest setVertexPropertyRequest) {
    final var vertex =
        vertexRepository.findById(id).orElseThrow(HttpStatusExceptions::notFound);
    final var property = new VertexProperty();
    property.setVertex(vertex);
    property.setScope(setVertexPropertyRequest.scope());
    property.setKey(setVertexPropertyRequest.key());
    property.setValue(setVertexPropertyRequest.value());
    vertexPropertyRepository.save(property);
  }
}
