package broccoli.model.identity.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.PathVariable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link RemoveRoleFromUserRequest} class.
 *
 * @param request HTTP request
 * @param userId  User id
 * @param roleId  Role id
 */
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public record RemoveRoleFromUserRequest(HttpRequest<?> request,
                                        @PathVariable @NotBlank String userId,
                                        @PathVariable @NotBlank String roleId) {
}
