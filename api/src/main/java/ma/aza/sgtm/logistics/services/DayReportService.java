package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.DayReportCreateDto;
import ma.aza.sgtm.logistics.dtos.DayReportDto;
import ma.aza.sgtm.logistics.dtos.DayReportUpdateDto;
import ma.aza.sgtm.logistics.entities.DayReport;
import ma.aza.sgtm.logistics.mappers.DayReportMapper;
import ma.aza.sgtm.logistics.repositories.DayReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DayReportService {

    private final DayReportRepository dayReportRepository;
    private final DayReportMapper mapper;

    public DayReportDto create(DayReportCreateDto dayReport) {
        DayReport entity = mapper.toEntity(dayReport);
        return mapper.toDto(dayReportRepository.save(entity));
    }

    public DayReportDto update(Long id, DayReportUpdateDto dayReport) {
        DayReport existingDayReport = dayReportRepository.findById(id).orElseThrow();
        mapper.updateFromDto(dayReport, existingDayReport);
        return mapper.toDto(dayReportRepository.save(existingDayReport));
    }

    public DayReportDto getById(Long id) {
        return mapper.toDto(dayReportRepository.findById(id).orElseThrow());
    }

    public List<DayReportDto> getAll() {
        return mapper.toDtoList(dayReportRepository.findAll());
    }

    public Page<DayReportDto> search(Specification<DayReport> specification, Pageable pageable) {
        return dayReportRepository.findAll(specification, pageable).map(mapper::toDto);
    }

    public void delete(Long id) {
        DayReport existingDayReport = dayReportRepository.findById(id).orElseThrow();
        dayReportRepository.delete(existingDayReport);
    }

}
