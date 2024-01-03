package broccoli.model.identity.http.request;

import broccoli.common.validator.GoodUsername;
import broccoli.common.validator.StrongPassword;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.Email;
import org.keycloak.representations.idm.UserRepresentation;

/**
 * The {@link CreateUserRequest} class.
 *
 * @param username  User username
 * @param password  User password
 * @param firstName User first name
 * @param lastName  User last name
 * @param email     User email
 */
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateUserRequest(@GoodUsername String username, @StrongPassword String password,
                                String firstName, String lastName, @Email String email) {

  /**
   * Convert to {@link UserRepresentation}.
   *
   * @return {@link UserRepresentation}
   */
  public UserRepresentation toRepresentation() {

    final var userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(username);
    userRepresentation.setFirstName(firstName);
    userRepresentation.setLastName(lastName);
    userRepresentation.setEmail(email);
    userRepresentation.setEnabled(true);
    return userRepresentation;
  }
}
