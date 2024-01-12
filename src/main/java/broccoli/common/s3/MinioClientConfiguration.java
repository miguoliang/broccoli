package broccoli.common.s3;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link MinioClientConfiguration} class.
 */
@ConfigurationProperties("minio")
public record MinioClientConfiguration(@NotBlank String endpoint, @NotBlank String accessKey,
                                       @NotBlank String secretKey) {
}
