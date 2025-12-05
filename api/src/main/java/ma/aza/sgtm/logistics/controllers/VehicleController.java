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
import ma.aza.sgtm.logistics.dtos.VehicleCreateDto;
import ma.aza.sgtm.logistics.dtos.VehicleDto;
import ma.aza.sgtm.logistics.dtos.VehicleUpdateDto;
import ma.aza.sgtm.logistics.dtos.VehicleWithDayReportsDto;
import ma.aza.sgtm.logistics.entities.Vehicle;
import ma.aza.sgtm.logistics.enums.VehicleType;
import ma.aza.sgtm.logistics.services.VehicleService;
import ma.aza.sgtm.logistics.specifications.VehicleSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Vehicles", description = "CRUD and search endpoints for vehicles")
public class VehicleController {

    private final VehicleService service;

    @PostMapping
    @Operation(
            summary = "Create a vehicle",
            description = "Registers a new vehicle in the fleet.",
            responses = @ApiResponse(responseCode = "200", description = "Vehicle created",
                    content = @Content(schema = @Schema(implementation = VehicleDto.class)))
    )
    public VehicleDto create(@RequestBody VehicleCreateDto vehicleCreateDto) {
        return service.create(vehicleCreateDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a vehicle",
            description = "Updates the details of an existing vehicle by id.",
            parameters = @Parameter(name = "id", description = "Identifier of the vehicle", required = true),
            responses = @ApiResponse(responseCode = "200", description = "Vehicle updated",
                    content = @Content(schema = @Schema(implementation = VehicleDto.class)))
    )
    public VehicleDto update(@PathVariable Long id, @RequestBody VehicleUpdateDto vehicleUpdateDto) {
        return service.update(id, vehicleUpdateDto);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get vehicle by id",
            parameters = @Parameter(name = "id", description = "Identifier of the vehicle", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vehicle found",
                            content = @Content(schema = @Schema(implementation = VehicleDto.class))),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            }
    )
    public VehicleDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(
            summary = "List vehicles",
            description = "Returns all vehicles without pagination.",
            responses = @ApiResponse(responseCode = "200", description = "Vehicles returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VehicleDto.class))))
    )
    public List<VehicleDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search vehicles",
            description = "Search vehicles using filtering criteria and pagination.",
            parameters = {
                    @Parameter(name = "id", description = "Exact vehicle id"),
                    @Parameter(name = "name", description = "Vehicle name contains filter"),
                    @Parameter(name = "code", description = "Vehicle code equals filter"),
                    @Parameter(name = "codeContains", description = "Vehicle code contains filter"),
                    @Parameter(name = "type", description = "Vehicle type"),
                    @Parameter(name = "createdAtFrom", description = "Created on or after (ISO date-time)"),
                    @Parameter(name = "createdAtTo", description = "Created on or before (ISO date-time)"),
                    @Parameter(name = "updatedAtFrom", description = "Updated on or after (ISO date-time)"),
                    @Parameter(name = "updatedAtTo", description = "Updated on or before (ISO date-time)")
            }
    )
    public Page<VehicleDto> search(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String codeContains,
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false) LocalDateTime createdAtFrom,
            @RequestParam(required = false) LocalDateTime createdAtTo,
            @RequestParam(required = false) LocalDateTime updatedAtFrom,
            @RequestParam(required = false) LocalDateTime updatedAtTo,
            Pageable pageable) {
        Specification<Vehicle> spec = Specification
                .allOf(VehicleSpecifications.hasId(id))
                .and(VehicleSpecifications.nameContains(name))
                .and(VehicleSpecifications.codeEquals(code))
                .and(VehicleSpecifications.codeContains(codeContains))
                .and(VehicleSpecifications.hasType(type))
                .and(VehicleSpecifications.createdAtFrom(createdAtFrom))
                .and(VehicleSpecifications.createdAtTo(createdAtTo))
                .and(VehicleSpecifications.updatedAtFrom(updatedAtFrom))
                .and(VehicleSpecifications.updatedAtTo(updatedAtTo));
        return service.search(spec, pageable);
    }

    @GetMapping("/with-day-reports")
    @Operation(
            summary = "Vehicles with day reports",
            description = "Returns paginated vehicles with their day reports within an optional date range.",
            parameters = {
                    @Parameter(name = "from", description = "Start date for reports"),
                    @Parameter(name = "to", description = "End date for reports")
            }
    )
    public Page<VehicleWithDayReportsDto> getVehiclesWithDayReports(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Pageable pageable) {
        return service.getVehiclesWithDayReports(from, to, pageable);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete vehicle",
            description = "Removes a vehicle by id.",
            parameters = @Parameter(name = "id", description = "Identifier of the vehicle", required = true),
            responses = @ApiResponse(responseCode = "204", description = "Vehicle deleted")
    )
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
