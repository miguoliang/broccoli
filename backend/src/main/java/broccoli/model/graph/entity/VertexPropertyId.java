package broccoli.model.graph.entity;

import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The {@link VertexPropertyId}.
 */
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Introspected
public class VertexPropertyId {

  private Vertex vertex;
  private String scope;
  private String key;
}
