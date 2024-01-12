package broccoli.controller.onlyoffice;

import broccoli.common.HttpStatusExceptions;
import broccoli.common.s3.MinioDefaultBucketConfiguration;
import broccoli.model.onlyoffice.http.OnlyOfficeCallbackRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.inject.Inject;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@link OnlyOfficeController} class.
 */
@Slf4j
@Controller("/onlyoffice")
@ExecuteOn(TaskExecutors.BLOCKING)
public class OnlyOfficeController {

  private static final String STATUS_1_RESPONSE = "{\"error\":0}";

  private final MinioClient minioClient;

  private final MinioDefaultBucketConfiguration minioDefaultBucketConfiguration;

  /**
   * The {@link OnlyOfficeController} constructor.
   *
   * @param minioClient                     The {@link MinioClient} instance.
   * @param minioDefaultBucketConfiguration The {@link MinioDefaultBucketConfiguration} instance.
   */
  @Inject
  public OnlyOfficeController(MinioClient minioClient,
                              MinioDefaultBucketConfiguration minioDefaultBucketConfiguration) {
    this.minioClient = minioClient;
    this.minioDefaultBucketConfiguration = minioDefaultBucketConfiguration;
  }

  /**
   * Callback.
   *
   * @param request The {@link OnlyOfficeCallbackRequest} request.
   * @return The {@link String} response.
   */
  @Post("/callback")
  public String callback(@Body OnlyOfficeCallbackRequest request) {

    log.info("Received callback request: {}", request);
    return switch (request.status()) {
      // It is received every user connection to or disconnection from document co-editing. Their
      // callbackUrl is used.
      case 1 -> STATUS_1_RESPONSE;
      // It is received 10 seconds after the document is closed for editing with the identifier of
      // the user who was the last to send the changes to the document editing service.
      case 2, 3 -> handleStatus2Or3(request);
      // It is received after the document is closed for editing with no changes by the last user.
      case 4 -> "";
      // It is received when the force saving request is performed.
      case 6, 7 -> handleStatus2Or3(request);
      default -> throw new IllegalStateException("Unexpected value: " + request.status());
    };
  }

  private String handleStatus2Or3(OnlyOfficeCallbackRequest request) {

    try (final var inputStream = URI.create(request.url()).toURL().openStream()) {
      final var putObjectArgs = PutObjectArgs
          .builder()
          .bucket(minioDefaultBucketConfiguration.bucket())
          .object(request.key())
          .stream(inputStream, inputStream.available(), -1)
          .build();
      minioClient.putObject(putObjectArgs);
    } catch (Exception e) {
      log.error("Error while uploading file to Minio", e);
      throw HttpStatusExceptions.raw(500, "Error while uploading file to Minio");
    }
    return "";
  }
}
