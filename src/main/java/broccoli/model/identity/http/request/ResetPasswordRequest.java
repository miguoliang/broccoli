package broccoli.model.identity.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.PathVariable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link ResetPasswordRequest} class.
 *
 * @param request HTTP request
 * @param id      User id
 */
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public record ResetPasswordRequest(HttpRequest<?> request, @PathVariable @NotBlank String id) {
}
