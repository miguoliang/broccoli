package broccoli.common.onlyoffice;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * The {@link OnlyOfficeHelper} class.
 */
@Singleton
public class OnlyOfficeHelper {

  private final String secret;

  public OnlyOfficeHelper(@Value("${onlyoffice.secret}") @NotBlank String secret) {
    this.secret = secret;
  }

  /**
   * Signature.
   *
   * @param payload The {@link Map} payload.
   * @return The {@link String} signature.
   * @throws JOSEException If an error occurs.
   */
  public String signature(Map<String, Object> payload) throws JOSEException {
    final var signer = new MACSigner(secret);
    final var claimSetBuilder = new JWTClaimsSet.Builder();
    payload.forEach(claimSetBuilder::claim);
    final var claimSet = claimSetBuilder.build();
    final var signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimSet);
    signedJwt.sign(signer);
    return signedJwt.serialize();
  }
}
