package ma.aza.sgtm.logistics.mappers;

import ma.aza.sgtm.logistics.dtos.DayReportCreateDto;
import ma.aza.sgtm.logistics.dtos.DayReportDto;
import ma.aza.sgtm.logistics.dtos.DayReportUpdateDto;
import ma.aza.sgtm.logistics.entities.DayReport;
import ma.aza.sgtm.logistics.entities.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DayReportMapper {

    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "vehicleFromId")
    DayReport toEntity(DayReportCreateDto dto);

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleCode", source = "vehicle.code")
    DayReportDto toDto(DayReport entity);

    List<DayReportDto> toDtoList(List<DayReport> entities);

    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "vehicleFromId")
    void updateFromDto(DayReportUpdateDto dto, @MappingTarget DayReport entity);

    @Named("vehicleFromId")
    default Vehicle vehicleFromId(Long id) {
        return id == null ? null : Vehicle.builder().id(id).build();
    }
}
