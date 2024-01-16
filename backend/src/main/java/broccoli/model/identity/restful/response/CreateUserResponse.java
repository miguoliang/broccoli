package broccoli.model.identity.restful.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

/**
 * The {@link CreateUserResponse} class.
 *
 * @param id        User id
 * @param username  User username
 * @param firstName User first name
 * @param lastName  User last name
 * @param email     User email
 */

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateUserResponse(String id, String username, String firstName, String lastName,
                                 String email) {
}
