package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.DayReportCreateDto;
import ma.aza.sgtm.logistics.dtos.DayReportDto;
import ma.aza.sgtm.logistics.dtos.DayReportUpdateDto;
import ma.aza.sgtm.logistics.entities.DayReport;
import ma.aza.sgtm.logistics.enums.VehicleState;
import ma.aza.sgtm.logistics.services.DayReportService;
import ma.aza.sgtm.logistics.specifications.DayReportSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/day-reports")
@RequiredArgsConstructor
public class DayReportController {

    private final DayReportService dayReportService;

    @PostMapping
    public DayReportDto create(@RequestBody DayReportCreateDto dayReportCreateDto) {
        return dayReportService.create(dayReportCreateDto);
    }

    @PutMapping("/{id}")
    public DayReportDto update(@PathVariable Long id, @RequestBody DayReportUpdateDto dayReportUpdateDto) {
        return dayReportService.update(id, dayReportUpdateDto);
    }

    @GetMapping("/{id}")
    public DayReportDto getById(@PathVariable Long id) {
        return dayReportService.getById(id);
    }

    @GetMapping
    public List<DayReportDto> getAll() {
        return dayReportService.getAll();
    }

    @GetMapping("/search")
    public Page<DayReportDto> search(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) VehicleState state,
            @RequestParam(required = false) String workHours,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String vehicleCode,
            Pageable pageable) {
        Specification<DayReport> spec = Specification
                .where(DayReportSpecifications.hasId(id))
                .and(DayReportSpecifications.dateEquals(date))
                .and(DayReportSpecifications.dateFrom(dateFrom))
                .and(DayReportSpecifications.dateTo(dateTo))
                .and(DayReportSpecifications.hasState(state))
                .and(DayReportSpecifications.workHoursContains(workHours))
                .and(DayReportSpecifications.hasVehicleId(vehicleId))
                .and(DayReportSpecifications.hasVehicleCode(vehicleCode));
        return dayReportService.search(spec, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        dayReportService.delete(id);
    }
}
