package broccoli.controller.identity;

import broccoli.common.HttpStatusExceptions;
import broccoli.common.identity.KeycloakDefaultRealmConfiguration;
import broccoli.model.identity.http.request.AssignRoleToUserRequest;
import broccoli.model.identity.http.request.CreateUserRequest;
import broccoli.model.identity.http.request.DeleteUserRequest;
import broccoli.model.identity.http.request.QueryUserRequest;
import broccoli.model.identity.http.request.RemoveRoleFromUserRequest;
import broccoli.model.identity.http.request.ResetPasswordRequest;
import broccoli.model.identity.http.request.UpdateUserRequest;
import broccoli.model.identity.http.response.CreateUserResponse;
import broccoli.model.identity.http.response.QueryUserResponse;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.RequestBean;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;

/**
 * The {@link UserController} class.
 */
@Controller("/identity/user")
@Validated
public class UserController {

  private final Keycloak keycloak;

  private final String keycloakRealm;

  @Inject
  public UserController(Keycloak keycloak,
                        KeycloakDefaultRealmConfiguration keycloakDefaultRealmConfiguration) {
    this.keycloak = keycloak;
    this.keycloakRealm = keycloakDefaultRealmConfiguration.realm();
  }

  /**
   * Create a user.
   *
   * @param createUserRequest Basic user info
   * @return User just created
   */
  @Post
  @Status(HttpStatus.CREATED)
  public CreateUserResponse create(@Body @Valid CreateUserRequest createUserRequest) {
    final var response =
        keycloak.realm(keycloakRealm).users().create(createUserRequest.toRepresentation());
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

  /**
   * Query users.
   *
   * @param queryUserRequest Query conditions
   * @return Users
   */
  @Get("/{?q}")
  public Page<QueryUserResponse> query(@Valid @RequestBean QueryUserRequest queryUserRequest) {

    final var users = keycloak.realm(keycloakRealm).users().search(
        queryUserRequest.q(),
        Math.toIntExact(queryUserRequest.pageable().getOffset()),
        queryUserRequest.pageable().getSize());
    final var total = keycloak.realm(keycloakRealm).users().count();
    return Page.of(users.stream().map(userRepresentation -> new QueryUserResponse(
        userRepresentation.getId(),
        userRepresentation.getUsername(),
        userRepresentation.getEmail(),
        userRepresentation.getCreatedTimestamp(),
        userRepresentation.getFirstName(),
        userRepresentation.getLastName(),
        userRepresentation.isEnabled()
    )).toList(), queryUserRequest.pageable(), total);
  }

  /**
   * Delete user by id.
   *
   * @param deleteUserRequest Parameters
   */
  @Delete("/{id}")
  public void delete(@Valid @RequestBean DeleteUserRequest deleteUserRequest) {
    keycloak.realm(keycloakRealm).users().delete(deleteUserRequest.id());
  }

  /**
   * Update user by id.
   *
   * @param id                User id
   * @param updateUserRequest User info
   * @return User just updated
   */
  @Put("/{id}")
  public CreateUserResponse update(@PathVariable String id,
                                   @Body @Valid UpdateUserRequest updateUserRequest) {
    keycloak.realm(keycloakRealm).users().get(id)
        .update(updateUserRequest.toRepresentation(id));
    final var user = keycloak.realm(keycloakRealm).users().get(id).toRepresentation();
    return new CreateUserResponse(
        user.getId(),
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail()
    );
  }

  /**
   * Reset password.
   *
   * @param resetPasswordRequest Parameters
   */
  @Post("/{id}/password")
  @Status(HttpStatus.NO_CONTENT)
  public void resetPassword(@Valid @RequestBean ResetPasswordRequest resetPasswordRequest) {
    keycloak.realm(keycloakRealm).users().get(resetPasswordRequest.id())
        .executeActionsEmail(List.of("UPDATE_PASSWORD"));
  }

  /**
   * Assign role to user.
   *
   * @param assignRoleToUserRequest Parameters
   */
  @Post("/{userId}/role/{roleId}")
  @Status(HttpStatus.NO_CONTENT)
  public void assignRoleToUser(
      @Valid @RequestBean AssignRoleToUserRequest assignRoleToUserRequest) {
    final var role = keycloak.realm(keycloakRealm).roles().get(assignRoleToUserRequest.roleId())
        .toRepresentation();
    keycloak.realm(keycloakRealm).users().get(assignRoleToUserRequest.userId()).roles().realmLevel()
        .add(List.of(role));
  }

  /**
   * remove role from user.
   *
   * @param removeRoleFromUserRequest Parameters
   */
  @Delete("/{userId}/role/{roleId}")
  @Status(HttpStatus.NO_CONTENT)
  public void removeRoleFromUser(
      @Valid @RequestBean RemoveRoleFromUserRequest removeRoleFromUserRequest) {
    final var role = keycloak.realm(keycloakRealm).roles().get(removeRoleFromUserRequest.roleId())
        .toRepresentation();
    keycloak.realm(keycloakRealm).users().get(removeRoleFromUserRequest.userId()).roles()
        .realmLevel().remove(List.of(role));
  }
}
