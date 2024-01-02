package broccoli.model.graph.entity;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The {@link GeneralColumns} entity.
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class GeneralColumns {

  @DateCreated
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_created")
  private LocalDateTime dateCreated = LocalDateTime.now();

  @DateUpdated
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_updated")
  private LocalDateTime dateUpdated = LocalDateTime.now();
}
