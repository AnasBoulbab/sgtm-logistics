package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.entities.DayReport;
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

    public DayReport create(DayReport dayReport) {
        return dayReportRepository.save(dayReport);
    }

    public DayReport update(Long id, DayReport dayReport) {
        DayReport existingDayReport = dayReportRepository.findById(id).orElseThrow();
        // TODO : use mapper to update existing vehicle fields
        return dayReportRepository.save(dayReport);
    }

    public DayReport getById(Long id) {
        return dayReportRepository.findById(id).orElseThrow();
    }

    public List<DayReport> getAll() {
        return dayReportRepository.findAll();
    }

    public Page<DayReport> search(Specification<DayReport> specification, Pageable pageable) {
        return dayReportRepository.findAll(specification, pageable);
    }

    public void delete(Long id) {
        DayReport existingDayReport = dayReportRepository.findById(id).orElseThrow();
        dayReportRepository.delete(existingDayReport);
    }

}
