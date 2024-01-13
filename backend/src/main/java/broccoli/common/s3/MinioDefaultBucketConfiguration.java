package broccoli.common.s3;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link MinioDefaultBucketConfiguration} class.
 */
@ConfigurationProperties("minio.default")
public record MinioDefaultBucketConfiguration(@NotBlank String bucket) {
}
