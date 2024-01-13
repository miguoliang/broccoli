package broccoli.common;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import org.junit.jupiter.api.TestInfo;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;

/**
 * The {@link IdentityTestHelper} class.
 */
@Singleton
public class IdentityTestHelper {

  @Inject
  private Keycloak keycloak;

  /**
   * Create username by a given {@link TestInfo}.
   *
   * @param testInfo {@link TestInfo}
   * @return Username
   */
  public String username(TestInfo testInfo) {

    final var testClass = testInfo.getTestClass().orElseThrow();
    final var className = testClass.getName().replaceAll("\\.", "_");
    return String.format("%s_%s", className,
        testInfo.getDisplayName().replaceAll("[()]", ""));
  }

  public boolean userExists(String username) {
    return !keycloak.realm("master").users().search(username).isEmpty();
  }

  public String userId(String username) {
    return keycloak.realm("master").users().search(username).getFirst().getId();
  }

  public List<String> userRoles(String userId) {
    return keycloak.realm("master").users().get(userId).roles().realmLevel().listAll().stream()
        .map(RoleRepresentation::getName).toList();
  }

  /**
   * Get role id (created if not exists).
   *
   * @param role Role name
   * @return Role id
   */
  public String roleId(String role) {
    try {
      final var roleRepresentation = keycloak.realm("master").roles().get(role).toRepresentation();
      return roleRepresentation.getId();
    } catch (NotFoundException e) {
      final var roleRepresentation = new RoleRepresentation();
      roleRepresentation.setName(role);
      keycloak.realm("master").roles().create(roleRepresentation);
      return keycloak.realm("master").roles().get(role).toRepresentation().getId();
    }
  }

  /**
   * Update user email.
   *
   * @param userId User id
   * @param mail   New email
   */
  public void userEmail(String userId, String mail) {
    final var userRepresentation = keycloak.realm("master").users().get(userId).toRepresentation();
    userRepresentation.setEmail(mail);
    keycloak.realm("master").users().get(userId).update(userRepresentation);
  }
}
