package broccoli;

import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.response.CreateVertexResponse;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
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
}
