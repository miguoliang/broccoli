package broccoli.model.identity.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link AssignRoleToUserRequest} class.
 *
 * @param request HTTP request
 * @param userId  User id
 * @param roleId  Role id
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssignRoleToUserRequest(HttpRequest<?> request,
                                      @PathVariable @NotBlank String userId,
                                      @PathVariable @NotBlank String roleId) {
}
