package ma.aza.sgtm.logistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.aza.sgtm.logistics.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWithDayReportsDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private String code;
    private Long externalId;
    private VehicleType type;
    private List<DayReportDto> dayReports;
}
