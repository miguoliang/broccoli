package broccoli.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.GraphResourceClient;
import broccoli.common.GraphTestHelper;
import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.http.request.CreateVertexRequest;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
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
    helper.createVertex(name, type);

    // Execute
    final var id = Vertex.getId(name, type);
    final var found = Mono.from(client.getVertex(id)).block();

    // Verify
    assertNotNull(found);
    assertEquals(id, found.id());
    assertEquals(name, found.name());
    assertEquals(type, found.type());
  }

  @Test
  void testGetVertex_ShouldReturnNotFound(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";

    // Execute
    final var id = Vertex.getId(name, type);
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> Mono.from(client.getVertex(id)).block(),
        "Vertex not found");

    // Verify
    assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
  }

  @Test
  void testCreateVertex_ShouldReturnConflict(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    final var request = new CreateVertexRequest(name, type);
    helper.createVertex(name, type);

    // Execute
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> Mono.from(client.createVertex(request)).block(),
        "Vertex already exists");

    // Verify
    assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
  }

  @Test
  void testDeleteVertex_ShouldReturnNoContentIfNotExists(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";

    // Execute
    final var id = Vertex.getId(name, type);
    final var response = Mono.from(client.deleteVertex(id)).block();

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    assertFalse(helper.vertexExists(name, type));
  }

  @Test
  void testDeleteVertex_ShouldReturnNoContentIfExists(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var id = Vertex.getId(name, type);
    final var response = Mono.from(client.deleteVertex(id)).block();

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    assertFalse(helper.vertexExists(name, type));
  }
}
