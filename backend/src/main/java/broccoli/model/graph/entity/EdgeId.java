package broccoli.model.graph.entity;

import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The {@link EdgeId}.
 */

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Introspected
public class EdgeId {

  private Vertex inVertex;
  private Vertex outVertex;
  private String name;
  private String scope;
}
