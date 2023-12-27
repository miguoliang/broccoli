package broccoli.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class EdgeId implements Serializable {
  @ManyToOne(optional = false)
  @JoinColumn(name = "in_vertex_id")
  private Vertex inVertex;

  @ManyToOne(optional = false)
  @JoinColumn(name = "out_vertex_id")
  private Vertex outVertex;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "scope", nullable = false)
  private String scope = "default";

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    EdgeId entity = (EdgeId) o;
    return Objects.equals(this.inVertex, entity.inVertex) &&
      Objects.equals(this.outVertex, entity.outVertex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inVertex, outVertex);
  }

}
