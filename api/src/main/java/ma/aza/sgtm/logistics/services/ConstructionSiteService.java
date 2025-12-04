package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.entities.ConstructionSite;
import ma.aza.sgtm.logistics.repositories.ConstructionSiteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConstructionSiteService {

    private final ConstructionSiteRepository constructionSiteRepository;

    public ConstructionSite create(ConstructionSite constructionSite) {
        return constructionSiteRepository.save(constructionSite);
    }

    public ConstructionSite update(Long id, ConstructionSite constructionSite) {
        ConstructionSite existingConstructionSite = constructionSiteRepository.findById(id).orElseThrow();
        // TODO : use mapper to update existing vehicle fields
        return constructionSiteRepository.save(constructionSite);
    }

    public ConstructionSite getById(Long id) {
        return constructionSiteRepository.findById(id).orElseThrow();
    }

    public List<ConstructionSite> getAll() {
        return constructionSiteRepository.findAll();
    }

    public Page<ConstructionSite> search(Specification<ConstructionSite> specification, Pageable pageable) {
        return constructionSiteRepository.findAll(specification, pageable);
    }

    public void delete(Long id) {
        ConstructionSite existingConstructionSite = constructionSiteRepository.findById(id).orElseThrow();
        constructionSiteRepository.delete(existingConstructionSite);
    }

}
