package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.entities.ConstructionSite;
import ma.aza.sgtm.logistics.services.ConstructionSiteService;
import ma.aza.sgtm.logistics.specifications.ConstructionSiteSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/construction-sites")
@RequiredArgsConstructor
public class ConstructionSiteController {

    private final ConstructionSiteService service;

    @PostMapping
    public ConstructionSite create(@RequestBody ConstructionSite constructionSite) {
        return service.create(constructionSite);
    }

    @PutMapping("/{id}")
    public ConstructionSite update(@PathVariable Long id, @RequestBody ConstructionSite constructionSite) {
        return service.update(id, constructionSite);
    }

    @GetMapping("/{id}")
    public ConstructionSite getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ConstructionSite> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    public Page<ConstructionSite> search(
            @RequestParam(required = false) Long id,
            Pageable pageable) {
        Specification<ConstructionSite> spec = Specification
                .where(ConstructionSiteSpecifications.hasId(id));
        return service.search(spec, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
