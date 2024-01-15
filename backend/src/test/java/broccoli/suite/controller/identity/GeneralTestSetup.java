package broccoli.suite.controller.identity;

import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_CLIENT;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_PASSWORD;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_REALM;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_ADMIN_USERNAME;
import static org.keycloak.test.FluentTestsHelper.DEFAULT_KEYCLOAK_URL;

import broccoli.common.BaseDatabaseTest;
import broccoli.common.IdentityTestHelper;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.test.FluentTestsHelper;

/**
 * The {@link GeneralTestSetup} class.
 */
@MicronautTest(transactional = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class GeneralTestSetup extends BaseDatabaseTest {

  @Inject
  @Client("/")
  protected HttpClient client;

  @Inject
  protected IdentityTestHelper identityTestHelper;

  @Inject
  protected Keycloak keycloak;

  protected FluentTestsHelper fluentTestsHelper;

  @BeforeAll
  void beforeAll() throws IOException {

    identityTestHelper.roleId("user");
    identityTestHelper.roleId("user_premium");

    fluentTestsHelper = new FluentTestsHelper(
        DEFAULT_KEYCLOAK_URL,
        DEFAULT_ADMIN_USERNAME,
        DEFAULT_ADMIN_PASSWORD,
        DEFAULT_ADMIN_REALM,
        DEFAULT_ADMIN_CLIENT,
        DEFAULT_ADMIN_REALM
    );
    fluentTestsHelper.init();
  }
}
