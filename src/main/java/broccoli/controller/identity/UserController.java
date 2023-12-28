package broccoli.controller.identity;

import broccoli.common.HttpStatusExceptions;
import broccoli.model.identity.http.request.CreateUserRequest;
import broccoli.model.identity.http.request.UpdateUserRequest;
import broccoli.model.identity.http.response.CreateUserResponse;
import broccoli.model.identity.http.response.QueryUserResponse;
import io.micronaut.context.annotation.Value;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
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
                        @Value("${keycloak.realm:broccoli}") String keycloakRealm) {
    this.keycloak = keycloak;
    this.keycloakRealm = keycloakRealm;
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

  /**
   * Query users.
   *
   * @param q        query string
   * @param pageable page info
   * @return users
   */
  @Get
  public Page<QueryUserResponse> query(@QueryValue String q, Pageable pageable) {

    final var users = keycloak.realm(keycloakRealm).users().search(q,
        Math.toIntExact(pageable.getOffset()),
        pageable.getSize());
    final var total = keycloak.realm(keycloakRealm).users().count();
    return Page.of(users.stream().map(userRepresentation -> new QueryUserResponse(
        userRepresentation.getId(),
        userRepresentation.getUsername(),
        userRepresentation.getEmail(),
        userRepresentation.getCreatedTimestamp(),
        userRepresentation.getFirstName(),
        userRepresentation.getLastName(),
        userRepresentation.isEnabled()
    )).toList(), pageable, total);
  }

  /**
   * Delete user by id.
   *
   * @param id User id
   */
  @Delete("{id}")
  public void delete(@PathVariable String id) {
    keycloak.realm(keycloakRealm).users().delete(id);
  }

  /**
   * Update user by id.
   *
   * @param id                User id
   * @param updateUserRequest User info
   * @return User just updated
   */
  @Put("{id}")
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
   * Reset user password by id.
   *
   * @param id User id
   */
  @Post("/{id}/password")
  public void resetPassword(@PathVariable String id) {
    keycloak.realm(keycloakRealm).users().get(id).executeActionsEmail(List.of("UPDATE_PASSWORD"));
  }

  /**
   * Assign role to user.
   *
   * @param userId User id
   * @param roleId Role id
   */
  @Post("/{userId}/role/{roleId}")
  public void assignRoleToUser(String userId, String roleId) {
    final var role = keycloak.realm(keycloakRealm).roles().get(roleId).toRepresentation();
    keycloak.realm(keycloakRealm).users().get(userId).roles().realmLevel().add(List.of(role));
  }

  /**
   * Unassign role from user.
   *
   * @param userId User id
   * @param roleId Role id
   */
  @Delete("/{userId}/role/{roleId}")
  public void unassignRoleFromUser(String userId, String roleId) {
    final var role = keycloak.realm(keycloakRealm).roles().get(roleId).toRepresentation();
    keycloak.realm(keycloakRealm).users().get(userId).roles().realmLevel().remove(List.of(role));
  }
}
