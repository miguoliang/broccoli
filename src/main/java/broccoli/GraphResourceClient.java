package broccoli;

import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.response.CreateVertexResponse;
import broccoli.model.graph.http.response.GetVertexResponse;
import broccoli.model.identity.http.response.QueryVertexResponse;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
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
  Publisher<Page<QueryVertexResponse>> getVertices(@QueryValue String q, Pageable pageable);

  @Delete("vertex/{id}")
  Publisher<Void> deleteVertex(String id);
}
