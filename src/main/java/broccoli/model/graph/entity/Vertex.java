package broccoli.model.graph.entity;

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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The {@link Vertex} entity.
 */
@Getter
@Setter
@Entity
@Table(name = "vertex")
@NoArgsConstructor
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

  /**
   * Set vertex name.
   *
   * @param name Vertex name
   */
  public void setName(String name) {
    this.name = name;
    this.id = getId(name, type);
  }

  /**
   * Set vertex type.
   *
   * @param type Vertex type
   */
  public void setType(String type) {
    this.type = type;
    this.id = getId(name, type);
  }

  /**
   * Calculate vertex id.
   *
   * @param name Vertex name
   * @param type Vertex type
   * @return Vertex id
   */
  public static String getId(String name, String type) {
    return DigestUtils.sha512Hex(
        StringUtils.defaultString(type) + ":" + StringUtils.defaultString(name));
  }
}
