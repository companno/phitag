package de.garrafao.phitag.infrastructure.persistence.jpa.instance.usetripleinstance;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstanceRepository;
import de.garrafao.phitag.infrastructure.persistence.jpa.instance.usetripleinstance.query.UseTripleInstanceQueryJpa;

@Repository
public class UseTripleInstanceRepositoryBridge implements UseTripleInstanceRepository {
    
    private final UseTripleInstanceRepositoryJpa useTripleInstanceRepositoryJpa;

    @Autowired
    public UseTripleInstanceRepositoryBridge(UseTripleInstanceRepositoryJpa useTripleInstanceRepositoryJpa) {
        this.useTripleInstanceRepositoryJpa = useTripleInstanceRepositoryJpa;
    }

    @Override
    public List<UseTripleInstance> findByQuery(Query query) {
        return this.useTripleInstanceRepositoryJpa.findAll(new UseTripleInstanceQueryJpa(query));
    }

    @Override
    public Page<UseTripleInstance> findByQueryPaged(Query query, PageRequestWraper page) {
        return this.useTripleInstanceRepositoryJpa.findAll(new UseTripleInstanceQueryJpa(query), page.getPageRequest());
    }

    @Override
    public Optional<UseTripleInstance> findByIdInstanceidAndIdPhaseidNameAndIdPhaseidProjectidNameAndIdPhaseidProjectidOwnername(
            String instanceId, String phaseName, String projectName, String ownerName) {
        return this.useTripleInstanceRepositoryJpa.findByIdInstanceidAndIdPhaseidNameAndIdPhaseidProjectidNameAndIdPhaseidProjectidOwnername(instanceId, phaseName, projectName, ownerName);
    }

    @Override
    public UseTripleInstance save(UseTripleInstance useTripleInstance) {
        return this.useTripleInstanceRepositoryJpa.save(useTripleInstance);
    }

    
}
