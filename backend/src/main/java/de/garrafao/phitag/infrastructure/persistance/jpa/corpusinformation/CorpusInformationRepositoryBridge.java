package de.garrafao.phitag.infrastructure.persistance.jpa.corpusinformation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.garrafao.phitag.domain.corpusinformation.CorpusInformation;
import de.garrafao.phitag.domain.corpusinformation.CorpusInformationRepository;

@Repository
public class CorpusInformationRepositoryBridge implements CorpusInformationRepository {

    private final CorpusInformationRepositoryJpa corpusInformationRepositoryJpa;

    @Autowired
    public CorpusInformationRepositoryBridge(CorpusInformationRepositoryJpa corpusInformationRepositoryJpa) {
        this.corpusInformationRepositoryJpa = corpusInformationRepositoryJpa;
    }

    @Override
    public Optional<CorpusInformation> findById(final String id) {
        return corpusInformationRepositoryJpa.findById(id);
    }
    
}
