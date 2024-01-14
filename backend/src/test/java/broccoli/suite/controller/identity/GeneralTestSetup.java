package broccoli.suite.controller.identity;

import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.IdentityTestHelper;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.test.FluentTestsHelper;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The {@link GeneralTestSetup} class.
 */
@MicronautTest(transactional = false)
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class GeneralTestSetup extends BaseDatabaseTest implements TestPropertyProvider {

  @Inject
  @Client("/")
  protected HttpClient client;

  @Inject
  protected IdentityTestHelper identityTestHelper;

  @Inject
  protected Keycloak keycloak;

  protected FluentTestsHelper fluentTestsHelper;

  protected abstract String getAuthServerUrl();

  @BeforeAll
  void beforeAll() {

    identityTestHelper.roleId("user");
    identityTestHelper.roleId("user_premium");

    fluentTestsHelper = new FluentTestsHelper(
        getAuthServerUrl(),
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_CLIENT,
        DEFAULT_ADMIN_REALM
    );
    fluentTestsHelper.init();
  }

  @Override
  public @NonNull Map<String, String> getProperties() {
    return Map.of(
        "keycloak.admin-client.server-url", getAuthServerUrl(),
        "keycloak.admin-client.client-secret", DEFAULT_ADMIN_PASSWORD
    );
  }
}
