package broccoli.endpoint.restful.onlyoffice;

import broccoli.common.HttpStatusExceptions;
import broccoli.common.onlyoffice.OnlyOfficeHelper;
import broccoli.common.s3.MinioDefaultBucketConfiguration;
import broccoli.model.onlyoffice.rest.OnlyOfficeCallbackRequest;
import com.nimbusds.jose.JOSEException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.inject.Inject;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@link OnlyOfficeController} class.
 */
@Slf4j
@Controller("/onlyoffice")
@ExecuteOn(TaskExecutors.BLOCKING)
@Hidden
public class OnlyOfficeController {

  private static final String STATUS_1_RESPONSE = "{\"error\":0}";

  private final MinioClient minioClient;

  private final MinioDefaultBucketConfiguration minioDefaultBucketConfiguration;

  private final OnlyOfficeHelper onlyOfficeHelper;

  /**
   * The {@link OnlyOfficeController} constructor.
   *
   * @param minioClient                     The {@link MinioClient} instance.
   * @param minioDefaultBucketConfiguration The {@link MinioDefaultBucketConfiguration} instance.
   * @param onlyOfficeHelper                The {@link OnlyOfficeHelper} instance.
   */
  @Inject
  public OnlyOfficeController(MinioClient minioClient,
                              MinioDefaultBucketConfiguration minioDefaultBucketConfiguration,
                              OnlyOfficeHelper onlyOfficeHelper) {
    this.minioClient = minioClient;
    this.minioDefaultBucketConfiguration = minioDefaultBucketConfiguration;
    this.onlyOfficeHelper = onlyOfficeHelper;
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

  /**
   * Signatures.
   *
   * @param request The {@link HttpRequest} request.
   * @return The {@link String} response.
   */
  @Get("/signatures")
  public String signatures(HttpRequest<?> request) {
    final var payload = request.getParameters().asMap(String.class, Object.class);
    try {
      return onlyOfficeHelper.signature(payload);
    } catch (JOSEException e) {
      log.error("Error while signing payload", e);
      throw HttpStatusExceptions.raw(500, "Error while signing payload");
    }
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
