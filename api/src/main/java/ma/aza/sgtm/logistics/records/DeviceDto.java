package ma.aza.sgtm.logistics.records;

import java.time.Instant;

public record DeviceDto(
        Integer id,
        String name,
        String uniqueId,
        String status,
        Instant lastUpdate
        // add other fields you care about
) {}
