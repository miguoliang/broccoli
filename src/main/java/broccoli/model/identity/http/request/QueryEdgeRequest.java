package broccoli.model.identity.http.request;

import broccoli.model.graph.entity.Edge;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.data.jpa.repository.criteria.Specification;
import io.micronaut.data.model.Pageable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link QueryEdgeRequest} class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public final class QueryEdgeRequest implements Pageable {

  private @NotEmpty Set<String> vertexIds;
  private @NotEmpty Set<String> names;
  private @NotEmpty Set<String> scopes;
  private @Min(0) int number = 0;
  private @Min(1) @Max(1000) int size = 1000;

  public Specification<Edge> toSpecification() {
    return null;
  }
}
