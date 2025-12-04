package ma.aza.sgtm.logistics.repositories;

import ma.aza.sgtm.logistics.entities.DayReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface DayReportRepository extends JpaRepository<DayReport, Long>, JpaSpecificationExecutor<DayReport> {
    Optional<DayReport> findByVehicleIdAndDate(Long vehicleId, LocalDate date);
}
