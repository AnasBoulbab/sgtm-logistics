package ma.aza.sgtm.logistics.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.aza.sgtm.logistics.enums.VehicleType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(unique = true)
    private Long externalId;
    private String source;

    private String name;
    @Column(unique = true)
    private String code;
    private VehicleType type;

    @OneToMany(mappedBy = "vehicle")
    private List<DayReport> dayReports;

}