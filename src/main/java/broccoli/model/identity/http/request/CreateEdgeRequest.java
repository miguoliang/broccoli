package broccoli.model.identity.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link CreateEdgeRequest} class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateEdgeRequest(@NotBlank String inVertexId, @NotBlank String outVertexId,
                                @NotBlank String name, @NotBlank String scope) {
}
