package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.aza.sgtm.logistics.dtos.DeviceDto;
import ma.aza.sgtm.logistics.entities.DayReport;
import ma.aza.sgtm.logistics.entities.Vehicle;
import ma.aza.sgtm.logistics.repositories.DayReportRepository;
import ma.aza.sgtm.logistics.repositories.VehicleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GpswoxSyncService {

    private final GpswoxService gpswoxService;
    private final VehicleRepository vehicleRepository;
    private final DayReportRepository dayReportRepository;

    @Scheduled(cron = "${gpswox.sync.devices.cron:0 0 1 * * *}")
    public void syncDevices() {
        List<DeviceDto> remoteDevices = gpswoxService.getDevices();
        if (remoteDevices.isEmpty()) {
            log.info("No GPSWOX devices found to sync");
            return;
        }

        remoteDevices.forEach(deviceDto -> {
            Vehicle vehicle = vehicleRepository.findByExternalId(deviceDto.getId())
                    .orElseGet(() -> Vehicle.builder()
                            .externalId(deviceDto.getId())
                            .code("GPSWOX-" + deviceDto.getId())
                            .build());

            vehicle.setName(deviceDto.getName());
            vehicle.setExternalId(deviceDto.getId());
            if (vehicle.getCode() == null) {
                vehicle.setCode("GPSWOX-" + deviceDto.getId());
            }

            vehicleRepository.save(vehicle);
        });

        log.info("Completed GPSWOX device synchronization. Devices processed: {}", remoteDevices.size());
    }

    @Scheduled(cron = "${gpswox.sync.day-report.cron:0 5 0 * * *}")
    public void syncPreviousDayReports() {
        LocalDate targetDate = LocalDate.now().minusDays(1);
        syncDayReportsForDate(targetDate);
    }

    public void syncDayReportsForDate(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(LocalTime.MAX);

        List<Vehicle> vehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getExternalId() == null) {
                continue;
            }
            try {
                String engineHours = gpswoxService.getEngineHours(vehicle.getExternalId(), from, to);
                DayReport dayReport = dayReportRepository.findByVehicleIdAndDate(vehicle.getId(), date)
                        .orElseGet(() -> DayReport.builder()
                                .vehicle(vehicle)
                                .date(date)
                                .build());
                dayReport.setWorkHours(engineHours);
                dayReportRepository.save(dayReport);
            } catch (Exception ex) {
                log.warn("Failed to sync day report for vehicle {} on {}: {}", vehicle.getId(), date, ex.getMessage());
            }
        }
    }
}
