package broccoli.model.onlyoffice.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

/**
 * The {@link OnlyOfficeCallbackRequest} class.
 */
@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
public record OnlyOfficeCallbackRequest(String key,
                                        List<Action> actions,
                                        History history,
                                        String fileType,
                                        String changesUrl,
                                        String forceSaveType,
                                        int status,
                                        String url,
                                        String userData,
                                        List<String> users) {

  @Serdeable
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
  record Action(String type, String userId) {
  }

  @Serdeable
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
  record History(String created, String key, User user, Integer version) {
  }

  @Serdeable
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
  record User(String id, String name) {

  }
}