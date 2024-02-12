/**
 * In all science, the two greatest mysteries are what happened before creation.
 * Why did we have a big bang? What banged? Are there other universes, multiverses, before the Big Bang?
 * That's the outer space. The second mystery is inner space. What goes on behind your eyeballs?
 * We have a hundred billion neurons in our brain, as many as stars in the Milky Way galaxy.
 * Each neuron is connected to ten thousand other neurons, and so what is the brain?
 */

package broccoli.suite.controller.graph;

import static io.micronaut.http.HttpRequest.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.GraphTestHelper;
import broccoli.model.graph.restful.request.CreateEdgeRequest;
import broccoli.model.graph.restful.response.CreateEdgeResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The {@link EdgeCreationTest} class.
 */
@MicronautTest(transactional = false)
class EdgeCreationTest extends BaseDatabaseTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    GraphTestHelper helper;

    @Test
    void shouldReturnCreated_WhenEdgeDoesNotExists(TestInfo testInfo) {

        // Setup
        final var inVertex = helper.createVertex(testInfo.getDisplayName() + "_in", "foo");
        final var outVertex = helper.createVertex(testInfo.getDisplayName() + "_out", "foo");
        final var name = "foo";
        final var scope = "bar";
        final var requestBody = new CreateEdgeRequest(inVertex.getId(), outVertex.getId(), name, scope);

        // Execute
        final var response =
                client.toBlocking().exchange(POST("api/graph/edge", requestBody), CreateEdgeResponse.class);

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
    void shouldReturnConflict_WhenEdgeAlreadyExists() {

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
                        .exchange(POST("api/graph/edge", requestBody), CreateEdgeResponse.class),
                "Conflict should be thrown");
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }
}
