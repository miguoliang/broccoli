package broccoli.model.identity.http.response;

import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link UpdateUserResponse} class.
 *
 * @param id        User id
 * @param username  User username
 * @param firstName User first name
 * @param lastName  User last name
 * @param email     User email
 */
@Serdeable
public record UpdateUserResponse(String id, String username, String firstName, String lastName,
                                 String email) {
}
