package broccoli.model.identity.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link DeleteUserRequest} class.
 *
 * @param request HTTP request
 * @param id      User id
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteUserRequest(HttpRequest<?> request, @PathVariable @NotBlank String id) {
}
