package broccoli.models;

import io.micronaut.configuration.hibernate.jpa.proxy.GenerateProxy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "vertex")
@GenerateProxy
public class Vertex {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToMany(mappedBy = "id.inVertex", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Edge> outEdges = new LinkedHashSet<>();

    @OneToMany(mappedBy = "id.outVertex", cascade = CascadeType.ALL, orphanRemoval = true)
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