package ma.aza.sgtm.logistics.specifications;

import ma.aza.sgtm.logistics.entities.DayReport;
import ma.aza.sgtm.logistics.enums.VehicleState;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DayReportSpecifications {

    public static Specification<DayReport> hasId(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<DayReport> dateEquals(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.equal(root.get("date"), date);
    }

    public static Specification<DayReport> dateFrom(LocalDate from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("date"), from);
    }

    public static Specification<DayReport> dateTo(LocalDate to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("date"), to);
    }

    public static Specification<DayReport> hasState(VehicleState state) {
        return (root, query, cb) ->
                state == null ? null : cb.equal(root.get("state"), state);
    }

    public static Specification<DayReport> workHoursContains(String workHours) {
        return (root, query, cb) ->
                (workHours == null || workHours.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("workHours")), "%" + workHours.toLowerCase() + "%");
    }

    public static Specification<DayReport> hasVehicleId(Long vehicleId) {
        return (root, query, cb) ->
                vehicleId == null ? null : cb.equal(root.get("vehicle").get("id"), vehicleId);
    }

    public static Specification<DayReport> hasVehicleCode(String vehicleCode) {
        return (root, query, cb) ->
                (vehicleCode == null || vehicleCode.isBlank())
                        ? null
                        : cb.equal(root.get("vehicle").get("code"), vehicleCode);
    }
}
