package de.garrafao.phitag.domain.instance.usetripleinstance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;

public interface UseTripleInstanceRepository {

    List<UseTripleInstance> findByQuery(final Query query);

    Page<UseTripleInstance> findByQueryPaged(final Query query, final PageRequestWraper page);
    
    Optional<UseTripleInstance> findByIdInstanceidAndIdPhaseidNameAndIdPhaseidProjectidNameAndIdPhaseidProjectidOwnername(final String instanceId, final String phaseName, final String projectName, final String ownerName);
    
    UseTripleInstance save(UseTripleInstance instanceData);


}
