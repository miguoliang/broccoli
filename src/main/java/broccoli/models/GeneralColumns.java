package broccoli.models;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@Serdeable
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
