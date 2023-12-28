package broccoli.controller.graph;

import static broccoli.common.HttpStatusExceptions.conflict;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.graph.repository.EdgeRepository;
import broccoli.model.graph.repository.VertexRepository;
import broccoli.model.identity.http.request.CreateEdgeRequest;
import broccoli.model.identity.http.request.QueryEdgeRequest;
import broccoli.model.identity.http.response.CreateEdgeResponse;
import broccoli.model.identity.http.response.QueryEdgeResponse;
import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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
   * @param queryEdgeRequest query edge request
   * @return page of query edge response
   */
  @Get
  public Page<QueryEdgeResponse> query(@Body @Valid QueryEdgeRequest queryEdgeRequest) {

    final var page = edgeRepository.findAll(queryEdgeRequest.toSpecification(), queryEdgeRequest);
    final var content = page.getContent().stream().map(QueryEdgeResponse::of).toList();
    return Page.of(content, page.getPageable(), page.getTotalSize());
  }
}
