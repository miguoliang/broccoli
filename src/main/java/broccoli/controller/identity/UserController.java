package broccoli.controller.identity;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.identity.http.request.CreateUserRequest;
import broccoli.model.identity.http.response.CreateUserResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;

/**
 * The {@link UserController} class.
 */
@Controller("/identity/user")
@Validated
public class UserController {

  private final Keycloak keycloak;

  @Inject
  public UserController(Keycloak keycloak) {
    this.keycloak = keycloak;
  }

  /**
   * Create a user.
   *
   * @param createUserRequest Basic user info
   * @return User just created
   */
  @Post
  public CreateUserResponse create(@Body @Valid CreateUserRequest createUserRequest) {
    final var response =
        keycloak.realm("broccoli").users().create(createUserRequest.toRepresentation());
    final var code = response.getStatus();
    if (code != 201) {
      throw HttpStatusExceptions.raw(code, response.getStatusInfo().getReasonPhrase());
    }
    final var userId = CreatedResponseUtil.getCreatedId(response);
    return new CreateUserResponse(
        userId,
        createUserRequest.username(),
        createUserRequest.firstName(),
        createUserRequest.lastName(),
        createUserRequest.email()
    );
  }
}
