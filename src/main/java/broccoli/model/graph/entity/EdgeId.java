package broccoli.model.graph.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;

/**
 * The {@link EdgeId}.
 */

@SuppressWarnings("unused")
@EqualsAndHashCode
public class EdgeId implements Serializable {

  private int inVertex;
  private int outVertex;
  private String name;
  private String scope;
}
