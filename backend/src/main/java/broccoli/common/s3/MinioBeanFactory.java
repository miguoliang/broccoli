package broccoli.common.s3;

import io.micronaut.context.annotation.Factory;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import jakarta.inject.Singleton;

/**
 * The {@link MinioBeanFactory} class.
 */
@Factory
public class MinioBeanFactory {

  /**
   * The {@link MinioClient} bean.
   *
   * @param minioClientConfiguration the {@link MinioClientConfiguration} instance.
   * @return the {@link MinioClient} instance.
   */
  @Singleton
  public MinioClient minioClient(MinioClientConfiguration minioClientConfiguration) {
    return MinioClient.builder()
        .endpoint(minioClientConfiguration.endpoint())
        .credentials(minioClientConfiguration.accessKey(), minioClientConfiguration.secretKey())
        .build();
  }

  /**
   * The {@link MinioAsyncClient} bean.
   *
   * @param minioClientConfiguration the {@link MinioClientConfiguration} instance.
   * @return the {@link MinioAsyncClient} instance.
   */
  @Singleton
  public MinioAsyncClient minioAsyncClient(MinioClientConfiguration minioClientConfiguration) {
    return MinioAsyncClient.builder()
        .endpoint(minioClientConfiguration.endpoint())
        .credentials(minioClientConfiguration.accessKey(), minioClientConfiguration.secretKey())
        .build();
  }
}
