package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteCreateDto;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteDto;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteUpdateDto;
import ma.aza.sgtm.logistics.entities.ConstructionSite;
import ma.aza.sgtm.logistics.mappers.ConstructionSiteMapper;
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
    private final ConstructionSiteMapper mapper;

    public ConstructionSiteDto create(ConstructionSiteCreateDto constructionSite) {
        ConstructionSite entity = mapper.toEntity(constructionSite);
        return mapper.toDto(constructionSiteRepository.save(entity));
    }

    public ConstructionSiteDto update(Long id, ConstructionSiteUpdateDto constructionSite) {
        ConstructionSite existingConstructionSite = constructionSiteRepository.findById(id).orElseThrow();
        mapper.updateFromDto(constructionSite, existingConstructionSite);
        return mapper.toDto(constructionSiteRepository.save(existingConstructionSite));
    }

    public ConstructionSiteDto getById(Long id) {
        return mapper.toDto(constructionSiteRepository.findById(id).orElseThrow());
    }

    public List<ConstructionSiteDto> getAll() {
        return mapper.toDtoList(constructionSiteRepository.findAll());
    }

    public Page<ConstructionSiteDto> search(Specification<ConstructionSite> specification, Pageable pageable) {
        return constructionSiteRepository.findAll(specification, pageable).map(mapper::toDto);
    }

    public void delete(Long id) {
        ConstructionSite existingConstructionSite = constructionSiteRepository.findById(id).orElseThrow();
        constructionSiteRepository.delete(existingConstructionSite);
    }

}
