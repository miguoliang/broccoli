package broccoli.endpoint.restful.identity;

import broccoli.common.HttpStatusExceptions;
import broccoli.common.identity.KeycloakDefaultRealmConfiguration;
import broccoli.model.identity.rest.request.AssignRoleToUserRequest;
import broccoli.model.identity.rest.request.CreateUserRequest;
import broccoli.model.identity.rest.request.DeleteUserRequest;
import broccoli.model.identity.rest.request.QueryUserRequest;
import broccoli.model.identity.rest.request.RemoveRoleFromUserRequest;
import broccoli.model.identity.rest.request.ResetPasswordRequest;
import broccoli.model.identity.rest.request.UpdateUserRequest;
import broccoli.model.identity.rest.response.CreateUserResponse;
import broccoli.model.identity.rest.response.QueryUserResponse;
import broccoli.model.identity.rest.response.UpdateUserResponse;
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
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;

/**
 * The {@link UserController} class.
 */
@Controller("/api/identity/user")
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
   * Search users.
   *
   * @param queryUserRequest Query conditions
   * @return Users
   */
  @Get("/{?q}")
  public Page<QueryUserResponse> search(@Valid @RequestBean QueryUserRequest queryUserRequest) {

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
  @Status(HttpStatus.NO_CONTENT)
  public void delete(@Valid @RequestBean DeleteUserRequest deleteUserRequest) {
    keycloak.realm(keycloakRealm).users().delete(deleteUserRequest.id());
  }

  /**
   * Update user by id. If you want to update username, you enable "Edit username" in the Realm
   * settings, otherwise, username is not allowed to be updated.
   *
   * @param id                User id
   * @param updateUserRequest User info
   * @return User just updated
   */
  @Put("/{id}")
  public UpdateUserResponse update(@PathVariable String id,
                                   @Body @Valid UpdateUserRequest updateUserRequest) {

    final var realmRepresentation = keycloak.realm(keycloakRealm).toRepresentation();
    final var user = keycloak.realm(keycloakRealm).users().get(id).toRepresentation();
    if (Boolean.FALSE.equals(realmRepresentation.isEditUsernameAllowed())
        && !user.getUsername().equals(updateUserRequest.username())) {
      throw HttpStatusExceptions.raw(HttpStatus.NOT_ACCEPTABLE,
          "Username is not allowed to be updated");
    }

    try {
      keycloak.realm(keycloakRealm).users().get(id)
          .update(updateUserRequest.toRepresentation(id));
    } catch (ClientErrorException e) {
      throw HttpStatusExceptions.raw(e.getResponse().getStatus(), e.getMessage());
    }

    final var updatedUser = keycloak.realm(keycloakRealm).users().get(id).toRepresentation();
    return new UpdateUserResponse(
        updatedUser.getId(),
        updatedUser.getUsername(),
        updatedUser.getFirstName(),
        updatedUser.getLastName(),
        updatedUser.getEmail()
    );
  }

  /**
   * Reset password. An email contains a link the user can click to perform a set of required
   * actions (UPDATE_PASSWORD).
   *
   * @param resetPasswordRequest Parameters
   */
  @Post("/{id}/reset-password")
  @Status(HttpStatus.NO_CONTENT)
  public void resetPassword(@Valid @RequestBean ResetPasswordRequest resetPasswordRequest) {
    try {
      keycloak.realm(keycloakRealm).users().get(resetPasswordRequest.id())
          .executeActionsEmail(List.of("UPDATE_PASSWORD"));
    } catch (NotFoundException e) {
      throw HttpStatusExceptions.notFound();
    }
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
    try {
      final var role =
          keycloak.realm(keycloakRealm).rolesById().getRole(assignRoleToUserRequest.roleId());
      keycloak.realm(keycloakRealm).users().get(assignRoleToUserRequest.userId()).roles()
          .realmLevel()
          .add(List.of(role));
    } catch (NotFoundException e) {
      throw HttpStatusExceptions.notFound();
    }
  }

  /**
   * Remove role from user.
   *
   * @param removeRoleFromUserRequest Parameters
   */
  @Delete("/{userId}/role/{roleId}")
  @Status(HttpStatus.NO_CONTENT)
  public void removeRoleFromUser(
      @Valid @RequestBean RemoveRoleFromUserRequest removeRoleFromUserRequest) {
    try {
      final var role =
          keycloak.realm(keycloakRealm).rolesById().getRole(removeRoleFromUserRequest.roleId());
      keycloak.realm(keycloakRealm).users().get(removeRoleFromUserRequest.userId()).roles()
          .realmLevel().remove(List.of(role));
    } catch (NotFoundException e) {
      throw HttpStatusExceptions.notFound();
    }
  }
}
