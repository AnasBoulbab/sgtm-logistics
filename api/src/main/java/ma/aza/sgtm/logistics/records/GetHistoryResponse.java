package ma.aza.sgtm.logistics.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetHistoryResponse(
        @JsonProperty("engine_hours") String engineHours
) {
}
