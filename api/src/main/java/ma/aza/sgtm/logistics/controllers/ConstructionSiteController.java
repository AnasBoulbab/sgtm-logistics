package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.entities.ConstructionSite;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteCreateDto;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteDto;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteUpdateDto;
import ma.aza.sgtm.logistics.services.ConstructionSiteService;
import ma.aza.sgtm.logistics.specifications.ConstructionSiteSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/construction-sites")
@RequiredArgsConstructor
public class ConstructionSiteController {

    private final ConstructionSiteService service;

    @PostMapping
    public ConstructionSiteDto create(@RequestBody ConstructionSiteCreateDto constructionSiteCreateDto) {
        return service.create(constructionSiteCreateDto);
    }

    @PutMapping("/{id}")
    public ConstructionSiteDto update(@PathVariable Long id, @RequestBody ConstructionSiteUpdateDto constructionSiteUpdateDto) {
        return service.update(id, constructionSiteUpdateDto);
    }

    @GetMapping("/{id}")
    public ConstructionSiteDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ConstructionSiteDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    public Page<ConstructionSiteDto> search(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime createdAtFrom,
            @RequestParam(required = false) LocalDateTime createdAtTo,
            @RequestParam(required = false) LocalDateTime updatedAtFrom,
            @RequestParam(required = false) LocalDateTime updatedAtTo,
            Pageable pageable) {
        Specification<ConstructionSite> spec = Specification
                .where(ConstructionSiteSpecifications.hasId(id))
                .and(ConstructionSiteSpecifications.nameContains(name))
                .and(ConstructionSiteSpecifications.descriptionContains(description))
                .and(ConstructionSiteSpecifications.createdAtFrom(createdAtFrom))
                .and(ConstructionSiteSpecifications.createdAtTo(createdAtTo))
                .and(ConstructionSiteSpecifications.updatedAtFrom(updatedAtFrom))
                .and(ConstructionSiteSpecifications.updatedAtTo(updatedAtTo));
        return service.search(spec, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
