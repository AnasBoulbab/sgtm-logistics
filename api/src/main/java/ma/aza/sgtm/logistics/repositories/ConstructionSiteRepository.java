package ma.aza.sgtm.logistics.repositories;

import ma.aza.sgtm.logistics.entities.ConstructionSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConstructionSiteRepository extends JpaRepository<ConstructionSite, Long>, JpaSpecificationExecutor<ConstructionSite> {
}
