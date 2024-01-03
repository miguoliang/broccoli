package broccoli.controller;

import static io.micronaut.http.HttpRequest.DELETE;
import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.GraphTestHelper;
import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.http.request.CreateVertexRequest;
import broccoli.model.graph.http.response.CreateVertexResponse;
import broccoli.model.graph.http.response.GetVertexResponse;
import broccoli.model.graph.http.response.QueryVertexResponse;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The {@link VertexControllerTest} class.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
class VertexControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  GraphTestHelper helper;

  @Test
  void testCreateVertex_ShouldReturnCreated(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    final var request = new CreateVertexRequest(name, type);

    // Execute
    final var response =
        client.toBlocking().exchange(POST("graph/vertex", request), CreateVertexResponse.class);

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
  void testGetVertex_ShouldReturnFound(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var id = Vertex.getId(name, type);
    final var found = client.toBlocking().exchange(GET("graph/vertex/" + id),
        GetVertexResponse.class);

    // Verify response status
    assertNotNull(found);
    assertEquals(HttpStatus.OK, found.getStatus());

    // Verify response body
    final var foundBody = found.body();
    assertNotNull(foundBody);
    assertEquals(id, foundBody.id());
    assertEquals(name, foundBody.name());
    assertEquals(type, foundBody.type());
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
        () -> client.toBlocking().exchange(GET("graph/vertex/" + id), GetVertexResponse.class),
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
        () -> client.toBlocking().exchange(POST("graph/vertex", request),
            CreateVertexResponse.class),
        "Vertex already exists");

    // Verify
    assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
  }

  @Test
  void testDeleteVertex_ShouldReturnNoContentIfNotExists(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    final var id = Vertex.getId(name, type);

    // Execute
    final var response = client.toBlocking().exchange(DELETE("graph/vertex/" + id));

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
    final var response = client.toBlocking().exchange(DELETE("graph/vertex/" + id));

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    assertFalse(helper.vertexExists(name, type));
  }

  @Test
  void testQueryVertices_ShouldReturnFoundWithQ(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var response = client.toBlocking().exchange(GET("graph/vertex?q=" + name),
        Argument.of(Page.class, QueryVertexResponse.class));

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatus());

    final var queryVertexResponse = response.body();
    assertNotNull(queryVertexResponse);
    assertEquals(1, queryVertexResponse.getTotalSize());
    assertEquals(1, queryVertexResponse.getContent().size());

    final var foundVertex = (QueryVertexResponse) queryVertexResponse.getContent().getFirst();
    assertEquals(Vertex.getId(name, type), foundVertex.id());
    assertEquals(name, foundVertex.name());
    assertEquals(type, foundVertex.type());
  }

  @Test
  void testQueryVertices_ShouldReturnFoundWithoutAnyParameters(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var response = client.toBlocking().exchange(GET("graph/vertex"),
        Argument.of(Page.class, QueryVertexResponse.class));

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatus());

    final var queryVertexResponse = response.body();
    assertNotNull(queryVertexResponse);
    assertTrue(1 <= queryVertexResponse.getTotalSize());
    assertTrue(10 >= queryVertexResponse.getContent().size());
  }

  @Test
  void testQueryVertices_ShouldReturnFoundWithPaginationOnly(TestInfo testInfo) {

    // Setup
    IntStream.range(0, 20).forEach(i -> helper.createVertex(testInfo.getDisplayName() + "_" + i,
        "foo"));

    // Execute
    final var response = client.toBlocking().exchange(GET("graph/vertex?page=1&size=5"),
        Argument.of(Page.class, QueryVertexResponse.class));

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatus());

    final var queryVertexResponse = response.body();
    assertNotNull(queryVertexResponse);
    assertEquals(1, queryVertexResponse.getPageNumber());
    assertEquals(5, queryVertexResponse.getSize());
    assertTrue(20 <= queryVertexResponse.getTotalSize());
    assertTrue(5 <= queryVertexResponse.getContent().size());
  }
}
