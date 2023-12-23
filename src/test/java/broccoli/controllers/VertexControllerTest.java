package broccoli.controllers;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import io.micronaut.http.client.annotation.*;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class VertexControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testIndex() throws Exception {
        assertEquals(HttpStatus.OK, client.toBlocking().exchange("/vertex").status());
    }
}
