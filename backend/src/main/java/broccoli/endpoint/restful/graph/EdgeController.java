package broccoli.endpoint.restful.graph;

import static broccoli.common.HttpStatusExceptions.conflict;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.graph.entity.Edge;
import broccoli.model.graph.entity.EdgeId;
import broccoli.model.graph.repository.EdgeRepository;
import broccoli.model.graph.repository.VertexRepository;
import broccoli.model.graph.restful.request.CreateEdgeRequest;
import broccoli.model.graph.restful.response.CreateEdgeResponse;
import broccoli.model.graph.restful.response.QueryEdgeResponse;
import broccoli.model.graph.spec.EdgeSpecifications;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * The {@link EdgeController} class.
 */
@Controller("/api/graph/edge")
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
  @Status(HttpStatus.CREATED)
  public CreateEdgeResponse create(@Body @Valid CreateEdgeRequest createEdgeRequest) {

    final var inVertex = vertexRepository.findById(createEdgeRequest.inVertexId())
        .orElseThrow(HttpStatusExceptions::notFound);
    final var outVertex = vertexRepository.findById(createEdgeRequest.outVertexId())
        .orElseThrow(HttpStatusExceptions::notFound);

    final var edgeId = new EdgeId();
    edgeId.setInVertex(inVertex);
    edgeId.setOutVertex(outVertex);
    edgeId.setName(createEdgeRequest.name());
    edgeId.setScope(createEdgeRequest.scope());

    if (edgeRepository.existsById(edgeId)) {
      throw conflict();
    }

    final var edge = new Edge();
    edge.setInVertex(inVertex);
    edge.setOutVertex(outVertex);
    edge.setName(createEdgeRequest.name());
    edge.setScope(createEdgeRequest.scope());

    return CreateEdgeResponse.of(edgeRepository.save(edge));
  }

  /**
   * Search edges.
   *
   * @param vid      vertex id
   * @param name     edge name
   * @param scope    edge scope
   * @param pageable page info
   * @return edges
   */
  @Get
  @Transactional
  public Page<QueryEdgeResponse> search(@QueryValue @NotEmpty Set<String> vid,
                                        @QueryValue @NotEmpty Set<String> name,
                                        @QueryValue @NotEmpty Set<String> scope,
                                        @Nullable Pageable pageable) {

    final var specs =
        EdgeSpecifications.associatedWithVertices(vid).and(EdgeSpecifications.nameIn(name))
            .and(EdgeSpecifications.scopeIn(scope));
    return edgeRepository.findAll(specs, pageable).map(QueryEdgeResponse::of);
  }
}
