package ma.aza.sgtm.logistics.mappers;

import ma.aza.sgtm.logistics.dtos.ConstructionSiteCreateDto;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteDto;
import ma.aza.sgtm.logistics.dtos.ConstructionSiteUpdateDto;
import ma.aza.sgtm.logistics.entities.ConstructionSite;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConstructionSiteMapper {

    ConstructionSite toEntity(ConstructionSiteCreateDto dto);

    ConstructionSiteDto toDto(ConstructionSite entity);

    List<ConstructionSiteDto> toDtoList(List<ConstructionSite> entities);

    void updateFromDto(ConstructionSiteUpdateDto dto, @MappingTarget ConstructionSite entity);
}
