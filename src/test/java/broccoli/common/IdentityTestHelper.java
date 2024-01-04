package broccoli.common;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.TestInfo;
import org.keycloak.admin.client.Keycloak;

/**
 * The {@link IdentityTestHelper} class.
 */
@Singleton
public class IdentityTestHelper {

  @Inject
  private Keycloak keycloak;

  public String username(TestInfo testInfo) {

    return testInfo.getDisplayName().replaceAll("[()]", "");
  }

  public boolean userExists(String username) {
    return !keycloak.realm("master").users().search(username).isEmpty();
  }

  public String userId(String username) {
    return keycloak.realm("master").users().search(username).getFirst().getId();
  }
}
