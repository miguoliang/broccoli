package broccoli.models;

import io.micronaut.configuration.hibernate.jpa.proxy.GenerateProxy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link Vertex} entity.
 */
@Getter
@Setter
@Entity
@Table(name = "vertex")
@GenerateProxy
public class Vertex {
  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @OneToMany(mappedBy = "inVertex", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Edge> outEdges = new LinkedHashSet<>();

  @OneToMany(mappedBy = "outVertex", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Edge> inEdges = new LinkedHashSet<>();

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "type", nullable = false)
  private String type;

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;
}
