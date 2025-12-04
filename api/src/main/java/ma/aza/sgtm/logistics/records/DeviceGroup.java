package ma.aza.sgtm.logistics.records;

import java.util.List;

public record DeviceGroup(
        String title,
        List<DeviceItem> items
) {}

