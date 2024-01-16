package broccoli.model.graph.restful.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@link CreateEdgeRequest} class.
 *
 * @param inVertexId  In vertex id
 * @param outVertexId Out vertex id
 * @param name        Edge name
 * @param scope       Edge scope
 */
@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateEdgeRequest(@NotBlank String inVertexId, @NotBlank String outVertexId,
                                @NotBlank String name, @NotBlank String scope) {
}
