package ma.aza.sgtm.logistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.aza.sgtm.logistics.enums.VehicleState;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayReportDto {
    private Long id;
    private LocalDate date;
    private VehicleState state;
    private String workHours;
    private Long vehicleId;
    private String vehicleCode;
}
