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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Construction Sites", description = "Manage construction sites")
public class ConstructionSiteController {

    private final ConstructionSiteService service;

    @PostMapping
    @Operation(
            summary = "Create construction site",
            description = "Creates a new construction site.",
            responses = @ApiResponse(responseCode = "200", description = "Site created",
                    content = @Content(schema = @Schema(implementation = ConstructionSiteDto.class)))
    )
    public ConstructionSiteDto create(@RequestBody ConstructionSiteCreateDto constructionSiteCreateDto) {
        return service.create(constructionSiteCreateDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update construction site",
            description = "Updates an existing construction site by id.",
            parameters = @Parameter(name = "id", description = "Construction site id", required = true),
            responses = @ApiResponse(responseCode = "200", description = "Site updated",
                    content = @Content(schema = @Schema(implementation = ConstructionSiteDto.class)))
    )
    public ConstructionSiteDto update(@PathVariable Long id, @RequestBody ConstructionSiteUpdateDto constructionSiteUpdateDto) {
        return service.update(id, constructionSiteUpdateDto);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get construction site",
            parameters = @Parameter(name = "id", description = "Construction site id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Site found",
                            content = @Content(schema = @Schema(implementation = ConstructionSiteDto.class))),
                    @ApiResponse(responseCode = "404", description = "Site not found")
            }
    )
    public ConstructionSiteDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(
            summary = "List construction sites",
            description = "Returns all construction sites.",
            responses = @ApiResponse(responseCode = "200", description = "Sites returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ConstructionSiteDto.class))))
    )
    public List<ConstructionSiteDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search construction sites",
            description = "Search construction sites using filters and pagination.",
            parameters = {
                    @Parameter(name = "id", description = "Exact construction site id"),
                    @Parameter(name = "name", description = "Name contains filter"),
                    @Parameter(name = "description", description = "Description contains filter"),
                    @Parameter(name = "createdAtFrom", description = "Created on or after (ISO date-time)"),
                    @Parameter(name = "createdAtTo", description = "Created on or before (ISO date-time)"),
                    @Parameter(name = "updatedAtFrom", description = "Updated on or after (ISO date-time)"),
                    @Parameter(name = "updatedAtTo", description = "Updated on or before (ISO date-time)")
            }
    )
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
    @Operation(
            summary = "Delete construction site",
            description = "Deletes a construction site by id.",
            parameters = @Parameter(name = "id", description = "Construction site id", required = true),
            responses = @ApiResponse(responseCode = "204", description = "Site deleted")
    )
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
