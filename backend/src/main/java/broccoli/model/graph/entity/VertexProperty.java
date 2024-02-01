package broccoli.model.graph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The {@link VertexProperty}.
 */
@Getter
@Setter
@Entity
@IdClass(VertexPropertyId.class)
@Table(name = "vertex_property")
@NoArgsConstructor
public class VertexProperty {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "vertex_id")
  private Vertex vertex;

  @Id
  @Column(name = "scope", nullable = false)
  private String scope = "default";

  @Id
  @Column(name = "property_key", nullable = false)
  private String key;

  @Column(name = "property_value", nullable = false)
  private String value = "";

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;
}
