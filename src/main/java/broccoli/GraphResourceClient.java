package broccoli;

import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.response.CreateVertexResponse;
import broccoli.model.graph.http.response.GetVertexResponse;
import broccoli.model.identity.http.response.QueryEdgeResponse;
import broccoli.model.identity.http.response.QueryVertexResponse;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import jakarta.annotation.Nullable;
import java.util.Set;
import org.reactivestreams.Publisher;

/**
 * The {@link GraphResourceClient} class.
 */
@Client("/graph")
public interface GraphResourceClient {

  @Post("vertex")
  @SingleResult
  Publisher<CreateVertexResponse> createVertex(@Body CreateVertexRequest request);

  @Get("vertex/{id}")
  @SingleResult
  Publisher<GetVertexResponse> getVertex(@PathVariable String id);

  @Get("vertex")
  @SingleResult
  Publisher<Page<QueryVertexResponse>> queryVertices(@QueryValue String q, Pageable pageable);

  @Delete("vertex/{id}")
  Publisher<HttpResponse<Void>> deleteVertex(String id);

  @Get("edge")
  @SingleResult
  Publisher<Page<QueryEdgeResponse>> queryEdges(
      @QueryValue Set<String> vertexId,
      @QueryValue Set<String> name,
      @QueryValue Set<String> scope,
      @Nullable Pageable pageable);
}
