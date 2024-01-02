package broccoli.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.GraphResourceClient;
import broccoli.common.GraphTestHelper;
import broccoli.model.identity.http.request.CreateEdgeRequest;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

/**
 * The {@link EdgeControllerTest} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EdgeControllerTest {

  @Inject
  GraphResourceClient client;

  @Inject
  GraphTestHelper helper;

  @Test
  void testQueryEdges_ShouldReturnBadRequest() {

    // Execute & Verify
    final var exception = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> Mono.from(client.queryEdges(Collections.emptySet(), Set.of(), Set.of(), null)).block(),
        "Bad request should be thrown");
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
  }

  @Test
  void testCreateEdge_ShouldReturnCreated(TestInfo testInfo) {

    // Setup
    final var inVertex = helper.createVertex(testInfo.getDisplayName() + "_in", "foo");
    final var outVertex = helper.createVertex(testInfo.getDisplayName() + "_out", "foo");
    final var name = "foo";
    final var scope = "bar";
    final var request = new CreateEdgeRequest(inVertex.getId(), outVertex.getId(), name, scope);

    // Execute
    final var response = Mono.from(client.createEdge(request)).block();

    // Verify the response
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatus());

    final var createdEdge = response.getBody().orElseThrow();
    assertEquals(inVertex.getId(), createdEdge.inVertexId());
    assertEquals(outVertex.getId(), createdEdge.outVertexId());
    assertEquals(name, createdEdge.name());
    assertEquals(scope, createdEdge.scope());

    // Verify the edge is created
    helper.edgeExists(inVertex.getId(), outVertex.getId(), name, scope);
  }
}
