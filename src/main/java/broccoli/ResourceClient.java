package broccoli;

import broccoli.model.http.request.CreateVertexRequest;
import broccoli.model.http.response.CreateVertexResponse;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

/**
 * The {@link ResourceClient} class.
 */
@Client("/")
public interface ResourceClient {

  @Post("vertex")
  @SingleResult
  Publisher<CreateVertexResponse> createVertex(@Body CreateVertexRequest request);
}
