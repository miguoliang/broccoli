package broccoli.suite.controller.graph;

import static io.micronaut.http.HttpRequest.DELETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.GraphTestHelper;
import broccoli.model.graph.entity.Vertex;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * The {@link VertexDeletionTest} class.
 */
@MicronautTest(transactional = false)
class VertexDeletionTest extends BaseDatabaseTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  GraphTestHelper helper;

  @Test
  void shouldReturnNoContent_WhenVertexDoesNotExist(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    final var id = Vertex.getId(name, type);

    // Execute
    final var response = client.toBlocking().exchange(DELETE("api/graph/vertex/" + id));

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    assertFalse(helper.vertexExists(name, type));
  }

  @Test
  void shouldReturnNoContent_WhenVertexAlreadyExists(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var id = Vertex.getId(name, type);
    final var response = client.toBlocking().exchange(DELETE("api/graph/vertex/" + id));

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    assertFalse(helper.vertexExists(name, type));
  }
}
