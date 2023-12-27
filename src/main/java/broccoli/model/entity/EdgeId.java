package broccoli.model.entity;

import java.io.Serializable;

/**
 * The {@link EdgeId}.
 */

@SuppressWarnings("unused")
public class EdgeId implements Serializable {

  private int inVertex;
  private int outVertex;
  private String name;
  private String scope;
}
