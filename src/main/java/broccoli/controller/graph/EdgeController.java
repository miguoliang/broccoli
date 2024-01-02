package broccoli.controller.graph;

import static broccoli.common.HttpStatusExceptions.conflict;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.graph.repository.EdgeRepository;
import broccoli.model.graph.repository.VertexRepository;
import broccoli.model.graph.spec.EdgeSpecifications;
import broccoli.model.identity.http.request.CreateEdgeRequest;
import broccoli.model.identity.http.response.CreateEdgeResponse;
import broccoli.model.identity.http.response.QueryEdgeResponse;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * The {@link EdgeController} class.
 */
@Controller("/graph/edge")
@Validated
public class EdgeController {

  private final VertexRepository vertexRepository;
  private final EdgeRepository edgeRepository;

  @Inject
  public EdgeController(VertexRepository vertexRepository, EdgeRepository edgeRepository) {
    this.vertexRepository = vertexRepository;
    this.edgeRepository = edgeRepository;
  }

  /**
   * Create an edge.
   *
   * @param createEdgeRequest Basic edge info
   * @return Edge just created
   */
  @Post
  @Transactional
  public CreateEdgeResponse create(@Body @Valid CreateEdgeRequest createEdgeRequest) {

    if (edgeRepository.existsById(createEdgeRequest.toEdgeId())) {
      throw conflict();
    }

    final var inVertex = vertexRepository.findById(createEdgeRequest.inVertexId())
        .orElseThrow(HttpStatusExceptions::notFound);
    final var outVertex = vertexRepository.findById(createEdgeRequest.outVertexId())
        .orElseThrow(HttpStatusExceptions::notFound);
    final var edge = createEdgeRequest.toEntity(inVertex, outVertex);
    return CreateEdgeResponse.of(edgeRepository.save(edge));
  }

  /**
   * Query edges.
   *
   * @param vid      vertex id
   * @param name     edge name
   * @param scope    edge scope
   * @param pageable page info
   * @return edges
   */
  @Get
  public Page<QueryEdgeResponse> query(@QueryValue @NotEmpty Set<String> vid,
                                       @QueryValue @NotEmpty Set<String> name,
                                       @QueryValue @NotEmpty Set<String> scope,
                                       Pageable pageable) {

    if (vid.isEmpty() && name.isEmpty() && scope.isEmpty()) {
      throw HttpStatusExceptions.badRequest();
    }

    final var specs = EdgeSpecifications.associatedWithVertices(vid)
        .and(EdgeSpecifications.nameIn(name))
        .and(EdgeSpecifications.scopeIn(scope));
    return edgeRepository.findAll(specs, pageable).map(QueryEdgeResponse::of);
  }
}
