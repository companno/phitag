package de.garrafao.phitag.infrastructure.persistence.jpa.dictionary.dictionary;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import de.garrafao.phitag.domain.dictionary.dictionary.Dictionary;
import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryId;

public interface DictionaryRepositoryJpa extends JpaRepository<Dictionary, DictionaryId>, JpaSpecificationExecutor<Dictionary> {

    Page<Dictionary> findByName(final String name, final PageRequest page);

    Optional<Dictionary> findByIdNameAndIdUname(String name, String uname);
    
}
