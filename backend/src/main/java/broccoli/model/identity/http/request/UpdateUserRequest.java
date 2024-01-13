package broccoli.model.identity.http.request;

import broccoli.common.validator.GoodUsername;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.Email;
import org.keycloak.representations.idm.UserRepresentation;

/**
 * The {@link UpdateUserRequest} class.
 *
 * @param email     User email
 * @param firstName User first name
 * @param lastName  User last name
 */
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserRequest(@GoodUsername String username, @Email String email,
                                String firstName, String lastName) {

  /**
   * Convert to {@link UserRepresentation}.
   *
   * @param id User id
   * @return {@link UserRepresentation}
   */
  public UserRepresentation toRepresentation(String id) {

    final var user = new UserRepresentation();
    user.setId(id);
    user.setUsername(username);
    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    return user;
  }
}
