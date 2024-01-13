package broccoli.model.identity.http.response;

import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link QueryUserResponse} class.
 */
@Serdeable
public record QueryUserResponse(String id, String username, String email, Long createdTime,
                                String firstName, String lastName,
                                boolean enabled) {
}
