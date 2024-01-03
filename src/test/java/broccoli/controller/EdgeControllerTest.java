package broccoli.controller;

import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.GraphTestHelper;
import broccoli.model.graph.http.response.CreateEdgeResponse;
import broccoli.model.graph.http.response.QueryEdgeResponse;
import broccoli.model.identity.http.request.CreateEdgeRequest;
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

/**
 * The {@link EdgeControllerTest} class.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
class EdgeControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  GraphTestHelper helper;

  @Test
  void testQueryEdges_ShouldReturnBadRequest() {

    // Execute & Verify
    final var exception = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking().exchange("graph/edge"),
        "Bad request should be thrown");
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
  }

  @Test
  void testQueryEdges_ShouldReturnOk(TestInfo testInfo) {

    // Setup
    final var inVertex = helper.createVertex(testInfo.getDisplayName() + "_in", "foo");
    final var outVertex = helper.createVertex(testInfo.getDisplayName() + "_out", "foo");
    final var name = "foo";
    final var scope = "bar";
    helper.createEdge(inVertex.getId(), outVertex.getId(), name, scope);

    // Execute
    final var response = client.toBlocking().exchange(
        GET("graph/edge?vid=" + inVertex.getId() + "&vid=" + outVertex.getId()
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

  @Test
  void testCreateEdge_ShouldReturnCreated(TestInfo testInfo) {

    // Setup
    final var inVertex = helper.createVertex(testInfo.getDisplayName() + "_in", "foo");
    final var outVertex = helper.createVertex(testInfo.getDisplayName() + "_out", "foo");
    final var name = "foo";
    final var scope = "bar";
    final var requestBody = new CreateEdgeRequest(inVertex.getId(), outVertex.getId(), name, scope);

    // Execute
    final var response =
        client.toBlocking().exchange(POST("graph/edge", requestBody), CreateEdgeResponse.class);

    // Verify the response
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatus());

    final var createdEdge = response.body();
    assertNotNull(createdEdge);
    assertEquals(inVertex.getId(), createdEdge.inVertexId());
    assertEquals(outVertex.getId(), createdEdge.outVertexId());
    assertEquals(name, createdEdge.name());
    assertEquals(scope, createdEdge.scope());

    // Verify the edge is created
    final var exists = helper.edgeExists(inVertex.getId(), outVertex.getId(), name, scope);
    assertTrue(exists);
  }

  @Test
  void testCreateEdge_ShouldReturnConflict() {

    // Setup
    final var inVertex = helper.createVertex("foo_in", "foo");
    final var outVertex = helper.createVertex("foo_out", "foo");
    final var name = "foo";
    final var scope = "bar";
    final var requestBody = new CreateEdgeRequest(inVertex.getId(), outVertex.getId(), name, scope);
    helper.createEdge(inVertex.getId(), outVertex.getId(), name, scope);

    // Execute & Verify
    final var exception = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking()
            .exchange(POST("graph/edge", requestBody), CreateEdgeResponse.class),
        "Conflict should be thrown");
    assertEquals(HttpStatus.CONFLICT, exception.getStatus());
  }
}
