package broccoli.model.graph.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link EdgeId}.
 */

@SuppressWarnings("unused")
@EqualsAndHashCode
@Getter
@Setter
public class EdgeId implements Serializable {

  private String inVertex;
  private String outVertex;
  private String name;
  private String scope;
}
