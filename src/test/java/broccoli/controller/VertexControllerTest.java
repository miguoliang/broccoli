package broccoli.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import broccoli.GraphResourceClient;
import broccoli.common.GraphTestHelper;
import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.http.request.CreateVertexRequest;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

/**
 * The {@link VertexControllerTest} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VertexControllerTest {

  @Inject
  GraphResourceClient client;

  @Inject
  GraphTestHelper helper;

  @Test
  void testCreateVertex_ShouldReturnCreated(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    final var request = new CreateVertexRequest(name, type);

    // Execute
    final var response = Mono.from(client.createVertex(request)).block();

    // Verify
    assertNotNull(response);
    assertEquals(request.name(), response.name());
    assertEquals(request.type(), response.type());
    assertEquals(Vertex.getId(name, type), response.id());
  }

  @Test
  void testGetVertex_ShouldReturnFound(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.create(name, type);

    // Execute
    final var id = Vertex.getId(name, type);
    final var found = Mono.from(client.getVertex(id)).block();

    // Verify
    assertNotNull(found);
    assertEquals(id, found.id());
    assertEquals(name, found.name());
    assertEquals(type, found.type());
  }
}
