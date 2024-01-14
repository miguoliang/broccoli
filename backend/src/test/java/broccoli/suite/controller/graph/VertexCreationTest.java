package broccoli.suite.controller.graph;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.GraphTestHelper;
import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.response.CreateVertexResponse;
import io.micronaut.context.annotation.Property;
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
 * The {@link VertexCreationTest} class.
 */
@MicronautTest(transactional = false)
@Execution(ExecutionMode.CONCURRENT)
class VertexCreationTest extends BaseDatabaseTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  GraphTestHelper helper;

  @Test
  void shouldReturnCreated_WhenVertexDoesNotExist(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    final var request = new CreateVertexRequest(name, type);

    // Execute
    final var response =
        client.toBlocking().exchange(POST("api/graph/vertex", request), CreateVertexResponse.class);

    // Verify the response
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatus());

    final var createdResponse = response.body();
    assertNotNull(createdResponse);
    assertEquals(request.name(), createdResponse.name());
    assertEquals(request.type(), createdResponse.type());
    assertEquals(Vertex.getId(name, type), createdResponse.id());

    // Verify the created vertex
    final var createdVertex = helper.vertexExists(name, type);
    assertTrue(createdVertex);
  }

  @Test
  void shouldReturnConflict_WhenVertexAlreadyExists(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    final var request = new CreateVertexRequest(name, type);
    helper.createVertex(name, type);

    // Execute
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking().exchange(POST("api/graph/vertex", request),
            CreateVertexResponse.class),
        "Vertex already exists");

    // Verify
    assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
  }
}
