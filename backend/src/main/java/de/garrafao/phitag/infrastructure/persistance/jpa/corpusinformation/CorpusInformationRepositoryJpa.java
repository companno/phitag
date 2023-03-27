package de.garrafao.phitag.infrastructure.persistance.jpa.corpusinformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import de.garrafao.phitag.domain.corpusinformation.CorpusInformation;

public interface CorpusInformationRepositoryJpa extends JpaRepository<CorpusInformation, String>, JpaSpecificationExecutor<CorpusInformation> {
    
}
