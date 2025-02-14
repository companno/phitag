package de.garrafao.phitag.infrastructure.persistence.jpa.instance.usetripleinstance;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstanceId;

public interface UseTripleInstanceRepositoryJpa
        extends JpaRepository<UseTripleInstance, UseTripleInstanceId>, JpaSpecificationExecutor<UseTripleInstance> {

    Optional<UseTripleInstance> findByIdInstanceidAndIdPhaseidNameAndIdPhaseidProjectidNameAndIdPhaseidProjectidOwnername(
            String instanceId, String phaseName, String projectName, String ownerName);

}
