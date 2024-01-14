package broccoli.suite.controller.graph;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.GraphTestHelper;
import broccoli.model.graph.entity.Vertex;
import broccoli.model.graph.http.response.GetVertexResponse;
import broccoli.model.graph.http.response.QueryVertexResponse;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.data.runtime.config.DataConfiguration;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * The {@link VertexQueryTest} class.
 */
@MicronautTest(transactional = false)
@Execution(ExecutionMode.CONCURRENT)
class VertexQueryTest extends BaseDatabaseTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Inject
  GraphTestHelper helper;

  @Inject
  DataConfiguration.PageableConfiguration pageableConfiguration;

  @Test
  void getById_ShouldReturnFound(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var id = Vertex.getId(name, type);
    final var found = client.toBlocking().exchange(GET("api/graph/vertex/" + id),
        GetVertexResponse.class);

    // Verify response status
    assertNotNull(found, "Response should not be null");
    assertEquals(HttpStatus.OK, found.getStatus(), "Status should be OK");

    // Verify response body
    final var foundBody = found.body();
    assertNotNull(foundBody, "Response body should not be null");
    assertEquals(id, foundBody.id(), "Id does not match");
    assertEquals(name, foundBody.name(), "Name should be " + name);
    assertEquals(type, foundBody.type(), "Type should be foo");
  }

  @Test
  void getById_ShouldReturnNotFound(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";

    // Execute
    final var id = Vertex.getId(name, type);
    final var thrown = assertThrowsExactly(
        HttpClientResponseException.class,
        () -> client.toBlocking().exchange(GET("api/graph/vertex/" + id), GetVertexResponse.class),
        "Vertex not found");

    // Verify
    assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus(), "Status should be NOT_FOUND");
  }

  @Test
  void query_ShouldReturnFoundWithQ(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var response = client.toBlocking().exchange(GET("api/graph/vertex?q=" + name),
        Argument.of(Page.class, QueryVertexResponse.class));

    // Verify
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.OK, response.getStatus(), "Status should be OK");

    final var queryVertexResponse = response.body();
    assertNotNull(queryVertexResponse, "Response body should not be null");
    assertEquals(1, queryVertexResponse.getTotalSize(), "Total size should be 1");
    assertEquals(1, queryVertexResponse.getContent().size(), "Content size should be 1");

    final var foundVertex = (QueryVertexResponse) queryVertexResponse.getContent().getFirst();
    assertEquals(Vertex.getId(name, type), foundVertex.id(), "Id does not match");
    assertEquals(name, foundVertex.name(), "Name should be " + name);
    assertEquals(type, foundVertex.type(), "Type should be foo");
  }

  @Test
  void query_ShouldReturnFoundWithoutParameters(TestInfo testInfo) {

    // Setup
    final var name = testInfo.getDisplayName();
    final var type = "foo";
    helper.createVertex(name, type);

    // Execute
    final var response = client.toBlocking().exchange(GET("api/graph/vertex"),
        Argument.of(Page.class, QueryVertexResponse.class));

    // Verify
    assertNotNull(response, "Response should not be null");
    assertEquals(HttpStatus.OK, response.getStatus(), "Status should be OK");

    final var queryVertexResponse = response.body();
    assertNotNull(queryVertexResponse, "Response body should not be null");
    assertTrue(1 <= queryVertexResponse.getTotalSize(), "Total size should be greater than 1");
    assertTrue(
        pageableConfiguration.getDefaultPageSize() >= queryVertexResponse.getContent().size(),
        "Content size should be less than 10");
  }

  @Test
  void query_ShouldReturnFoundWithPaginationOnly(TestInfo testInfo) {

    // Setup
    IntStream.range(0, 20).forEach(i -> helper.createVertex(testInfo.getDisplayName() + "_" + i,
        "foo"));

    // Execute
    final var response = client.toBlocking().exchange(GET("api/graph/vertex?page=1&size=5"),
        Argument.of(Page.class, QueryVertexResponse.class));

    // Verify
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatus());

    final var queryVertexResponse = response.body();
    assertNotNull(queryVertexResponse, "Response body should not be null");
    assertEquals(1, queryVertexResponse.getPageNumber(), "Page number should be 1");
    assertEquals(5, queryVertexResponse.getSize(), "Size should be 5");
    assertTrue(20 <= queryVertexResponse.getTotalSize(), "Total size should be greater than 20");
    assertTrue(5 <= queryVertexResponse.getContent().size(),
        "Content size should be greater than 5");
  }
}
