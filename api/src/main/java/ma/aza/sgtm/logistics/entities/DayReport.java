package ma.aza.sgtm.logistics.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.aza.sgtm.logistics.enums.VehicleState;

import java.time.LocalDate;

@Entity
@Table(name = "day_report", uniqueConstraints = @UniqueConstraint(columnNames = {"vehicle_id", "date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private VehicleState state;
    private String workHours;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

}
