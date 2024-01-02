package broccoli.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import broccoli.GraphResourceClient;
import broccoli.common.GraphTestHelper;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Set;
import org.junit.jupiter.api.Test;
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
        () -> Mono.from(client.queryEdges(Set.of(), Set.of(), Set.of(), null)).block(),
        "Bad request should be thrown");
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
  }
}
