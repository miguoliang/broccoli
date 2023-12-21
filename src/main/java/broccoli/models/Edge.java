package broccoli.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "edge")
public class Edge {
    @EmbeddedId
    private EdgeId id;

    @Embedded
    private GeneralColumns generalColumns;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 0;
}