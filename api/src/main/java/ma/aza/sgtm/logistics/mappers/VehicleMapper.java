package ma.aza.sgtm.logistics.mappers;

import ma.aza.sgtm.logistics.dtos.VehicleCreateDto;
import ma.aza.sgtm.logistics.dtos.VehicleDto;
import ma.aza.sgtm.logistics.dtos.VehicleUpdateDto;
import ma.aza.sgtm.logistics.dtos.VehicleWithDayReportsDto;
import ma.aza.sgtm.logistics.entities.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DayReportMapper.class})
public interface VehicleMapper {

    Vehicle toEntity(VehicleCreateDto dto);

    VehicleDto toDto(Vehicle entity);

    List<VehicleDto> toDtoList(List<Vehicle> entities);

    void updateFromDto(VehicleUpdateDto dto, @MappingTarget Vehicle entity);

    VehicleWithDayReportsDto toDtoWithDayReports(Vehicle entity);
}
