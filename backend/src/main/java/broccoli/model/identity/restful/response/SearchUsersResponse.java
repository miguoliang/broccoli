package broccoli.model.identity.restful.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link SearchUsersResponse} class.
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchUsersResponse(String id, String username, String email, Long createdTime,
                                  String firstName, String lastName,
                                  boolean enabled) {
}
