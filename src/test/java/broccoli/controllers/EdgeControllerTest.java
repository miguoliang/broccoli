package broccoli.controllers;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import io.micronaut.http.client.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class EdgeControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testIndex() {
        assertEquals(HttpStatus.OK, client.toBlocking().exchange("/edge").status());
    }
}
