package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.VehicleCreateDto;
import ma.aza.sgtm.logistics.dtos.VehicleDto;
import ma.aza.sgtm.logistics.dtos.VehicleUpdateDto;
import ma.aza.sgtm.logistics.dtos.VehicleWithDayReportsDto;
import ma.aza.sgtm.logistics.dtos.DayReportDto;
import ma.aza.sgtm.logistics.entities.DayReport;
import ma.aza.sgtm.logistics.entities.Vehicle;
import ma.aza.sgtm.logistics.mappers.DayReportMapper;
import ma.aza.sgtm.logistics.mappers.VehicleMapper;
import ma.aza.sgtm.logistics.repositories.DayReportRepository;
import ma.aza.sgtm.logistics.repositories.VehicleRepository;
import ma.aza.sgtm.logistics.specifications.DayReportSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DayReportRepository dayReportRepository;
    private final VehicleMapper mapper;
    private final DayReportMapper dayReportMapper;

    public VehicleDto create(VehicleCreateDto vehicle) {
        Vehicle entity = mapper.toEntity(vehicle);
        return mapper.toDto(vehicleRepository.save(entity));
    }

    public VehicleDto update(Long id, VehicleUpdateDto vehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElseThrow();
        mapper.updateFromDto(vehicle, existingVehicle);
        return mapper.toDto(vehicleRepository.save(existingVehicle));
    }

    public VehicleDto getById(Long id) {
        return mapper.toDto(vehicleRepository.findById(id).orElseThrow());
    }

    public List<VehicleDto> getAll() {
        return mapper.toDtoList(vehicleRepository.findAll());
    }

    public Page<VehicleDto> search(Specification<Vehicle> specification, Pageable pageable) {
        return vehicleRepository.findAll(specification, pageable).map(mapper::toDto);
    }

    public Page<VehicleWithDayReportsDto> getVehiclesWithDayReports(LocalDate from, LocalDate to, Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(vehicle -> {
            Specification<DayReport> spec = Specification
                    .where(DayReportSpecifications.hasVehicleId(vehicle.getId()))
                    .and(DayReportSpecifications.dateFrom(from))
                    .and(DayReportSpecifications.dateTo(to));
            List<DayReportDto> dayReports = dayReportRepository.findAll(spec).stream()
                    .map(dayReportMapper::toDto)
                    .toList();
            return mapper.toDtoWithDayReports(vehicle, dayReports);
        });
    }

    public void delete(Long id) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElseThrow();
        vehicleRepository.delete(existingVehicle);
    }

}
