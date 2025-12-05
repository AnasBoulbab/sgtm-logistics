package ma.aza.sgtm.logistics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Day Reports", description = "Manage day reports attached to vehicles")
public class DayReportController {

    private final DayReportService dayReportService;

    @PostMapping
    @Operation(
            summary = "Create day report",
            description = "Creates a day report for a vehicle.",
            responses = @ApiResponse(responseCode = "200", description = "Report created",
                    content = @Content(schema = @Schema(implementation = DayReportDto.class)))
    )
    public DayReportDto create(@RequestBody DayReportCreateDto dayReportCreateDto) {
        return dayReportService.create(dayReportCreateDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update day report",
            description = "Updates an existing day report by id.",
            parameters = @Parameter(name = "id", description = "Day report id", required = true),
            responses = @ApiResponse(responseCode = "200", description = "Report updated",
                    content = @Content(schema = @Schema(implementation = DayReportDto.class)))
    )
    public DayReportDto update(@PathVariable Long id, @RequestBody DayReportUpdateDto dayReportUpdateDto) {
        return dayReportService.update(id, dayReportUpdateDto);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get day report",
            parameters = @Parameter(name = "id", description = "Day report id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Report found",
                            content = @Content(schema = @Schema(implementation = DayReportDto.class))),
                    @ApiResponse(responseCode = "404", description = "Report not found")
            }
    )
    public DayReportDto getById(@PathVariable Long id) {
        return dayReportService.getById(id);
    }

    @GetMapping
    @Operation(
            summary = "List day reports",
            description = "Returns all day reports.",
            responses = @ApiResponse(responseCode = "200", description = "Reports returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DayReportDto.class))))
    )
    public List<DayReportDto> getAll() {
        return dayReportService.getAll();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search day reports",
            description = "Search day reports using filters and pagination.",
            parameters = {
                    @Parameter(name = "id", description = "Exact day report id"),
                    @Parameter(name = "date", description = "Date equals filter"),
                    @Parameter(name = "dateFrom", description = "Date from (inclusive)"),
                    @Parameter(name = "dateTo", description = "Date to (inclusive)"),
                    @Parameter(name = "state", description = "Vehicle state on the report"),
                    @Parameter(name = "workHours", description = "Work hours contains"),
                    @Parameter(name = "vehicleId", description = "Vehicle id filter"),
                    @Parameter(name = "vehicleCode", description = "Vehicle code filter")
            }
    )
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
    @Operation(
            summary = "Delete day report",
            description = "Deletes a day report by id.",
            parameters = @Parameter(name = "id", description = "Day report id", required = true),
            responses = @ApiResponse(responseCode = "204", description = "Report deleted")
    )
    public void delete(@PathVariable Long id) {
        dayReportService.delete(id);
    }
}
