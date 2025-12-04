package ma.aza.sgtm.logistics.specifications;

import ma.aza.sgtm.logistics.entities.Vehicle;
import ma.aza.sgtm.logistics.enums.VehicleType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class VehicleSpecifications {

    public static Specification<Vehicle> hasId(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Vehicle> nameContains(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Vehicle> codeEquals(String code) {
        return (root, query, cb) ->
                (code == null || code.isBlank())
                        ? null
                        : cb.equal(root.get("code"), code);
    }

    public static Specification<Vehicle> codeContains(String code) {
        return (root, query, cb) ->
                (code == null || code.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
    }

    public static Specification<Vehicle> hasType(VehicleType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Vehicle> createdAtFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<Vehicle> createdAtTo(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<Vehicle> updatedAtFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("updatedAt"), from);
    }

    public static Specification<Vehicle> updatedAtTo(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("updatedAt"), to);
    }
}
