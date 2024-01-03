package broccoli.model.graph.entity;

import jakarta.persistence.CascadeType;
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
 * The {@link Edge} entity.
 */
@Getter
@Setter
@Entity
@Table(name = "edge")
@IdClass(EdgeId.class)
@NoArgsConstructor
public class Edge {

  @Id
  @ManyToOne(optional = false, cascade = CascadeType.DETACH)
  @JoinColumn(name = "in_vertex_id")
  private Vertex inVertex;

  @Id
  @ManyToOne(optional = false, cascade = CascadeType.DETACH)
  @JoinColumn(name = "out_vertex_id")
  private Vertex outVertex;

  @Id
  @Column(name = "name", nullable = false)
  private String name;

  @Id
  @Column(name = "scope", nullable = false)
  private String scope = "default";

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;
}
