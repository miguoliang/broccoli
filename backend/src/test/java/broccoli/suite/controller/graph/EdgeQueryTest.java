package broccoli.suite.controller.graph;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.GraphTestHelper;
import broccoli.model.graph.http.response.QueryEdgeResponse;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * The {@link EdgeQueryTest} class.
 */
@MicronautTest(transactional = false)
class EdgeQueryTest extends BaseDatabaseTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  GraphTestHelper helper;

  @Test
  void shouldReturnBadRequest_WithoutParameters() {

    // Execute & Verify
    final var exception = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking().exchange("api/graph/edge"),
        "Bad request should be thrown");
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
  }

  @Test
  void shouldReturnOk_WithValidParameters(TestInfo testInfo) {

    // Setup
    final var inVertex = helper.createVertex(testInfo.getDisplayName() + "_in", "foo");
    final var outVertex = helper.createVertex(testInfo.getDisplayName() + "_out", "foo");
    final var name = "foo";
    final var scope = "bar";
    helper.createEdge(inVertex.getId(), outVertex.getId(), name, scope);

    // Execute
    final var response = client.toBlocking().exchange(
        GET("api/graph/edge?vid=" + inVertex.getId() + "&vid=" + outVertex.getId()
            + "&name=" + name + "&scope=" + scope),
        Argument.of(Page.class, QueryEdgeResponse.class));

    // Verify the response
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatus());

    final var queryEdgeResponse = response.body();
    assertNotNull(queryEdgeResponse);
    assertEquals(1, queryEdgeResponse.getTotalSize());
    assertEquals(1, queryEdgeResponse.getContent().size());

    final var foundEdge = (QueryEdgeResponse) queryEdgeResponse.getContent().getFirst();
    assertEquals(inVertex.getId(), foundEdge.inVertexId());
    assertEquals(outVertex.getId(), foundEdge.outVertexId());
    assertEquals(name, foundEdge.name());
    assertEquals(scope, foundEdge.scope());
  }
}
