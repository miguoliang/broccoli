package broccoli.suite.controller.graph;

import static io.micronaut.http.HttpRequest.PUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.GraphTestHelper;
import broccoli.common.TransactionHelper;
import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.restful.request.SetVertexPropertyRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The {@link VertexPropertyCreationTest} class.
 */
@MicronautTest(transactional = false)
class VertexPropertyCreationTest extends BaseDatabaseTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  GraphTestHelper helper;

  @Inject
  TransactionHelper transactionHelper;

  @Test
  void shouldSavePropertyValue_IfPropertyDoesNotExist(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);
    final var setVertexPropertyRequest = new SetVertexPropertyRequest("default", "key", "value");

    // Execute
    final var id = Vertex.getId(name, type);
    final var found = client.toBlocking().exchange(PUT("api/graph/vertex/" + id + "/property",
        setVertexPropertyRequest));

    // Verify response status
    assertNotNull(found, "Response should not be null");
    assertEquals(HttpStatus.NO_CONTENT, found.getStatus(), "Status should be NO_CONTENT");

    // Verify property
    transactionHelper.executeWithinTransaction(() -> {
      final var vertex = helper.getVertex(name, type);
      assertTrue(vertex.hasProperty("default", "key"), "Property should exist");
      final var values = vertex.getProperty("default", "key");
      assertEquals(1, values.size(), "Size should be 1");
      assertEquals(setVertexPropertyRequest.value(), values.getFirst(),
          String.format("Value should be '%s'", setVertexPropertyRequest.value()));
    });
  }
}
