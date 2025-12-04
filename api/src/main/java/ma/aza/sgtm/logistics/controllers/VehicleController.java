package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.VehicleCreateDto;
import ma.aza.sgtm.logistics.dtos.VehicleDto;
import ma.aza.sgtm.logistics.dtos.VehicleUpdateDto;
import ma.aza.sgtm.logistics.entities.Vehicle;
import ma.aza.sgtm.logistics.enums.VehicleType;
import ma.aza.sgtm.logistics.services.VehicleService;
import ma.aza.sgtm.logistics.specifications.VehicleSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;

    @PostMapping
    public VehicleDto create(@RequestBody VehicleCreateDto vehicleCreateDto) {
        return service.create(vehicleCreateDto);
    }

    @PutMapping("/{id}")
    public VehicleDto update(@PathVariable Long id, @RequestBody VehicleUpdateDto vehicleUpdateDto) {
        return service.update(id, vehicleUpdateDto);
    }

    @GetMapping("/{id}")
    public VehicleDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<VehicleDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
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
                .where(VehicleSpecifications.hasId(id))
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
