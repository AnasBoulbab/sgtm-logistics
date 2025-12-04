package ma.aza.sgtm.logistics.specifications;

import ma.aza.sgtm.logistics.entities.ConstructionSite;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ConstructionSiteSpecifications {

    public static Specification<ConstructionSite> hasId(Long id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<ConstructionSite> nameContains(String name) {
        return (root, query, cb) -> (name == null || name.isBlank()) ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ConstructionSite> descriptionContains(String description) {
        return (root, query, cb) -> (description == null || description.isBlank()) ? null : cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<ConstructionSite> createdAtFrom(LocalDateTime from) {
        return (root, query, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<ConstructionSite> createdAtTo(LocalDateTime to) {
        return (root, query, cb) -> to == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<ConstructionSite> updatedAtFrom(LocalDateTime from) {
        return (root, query, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("updatedAt"), from);
    }

    public static Specification<ConstructionSite> updatedAtTo(LocalDateTime to) {
        return (root, query, cb) -> to == null ? null : cb.lessThanOrEqualTo(root.get("updatedAt"), to);
    }
}
