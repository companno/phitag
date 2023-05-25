package de.garrafao.phitag.domain.dictionary.entry;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;

import de.garrafao.phitag.domain.core.Query;

public interface DictionaryEntryRepository {

    Page<DictionaryEntry> findAll(Pageable pageable);

    Page<DictionaryEntry> findAllByQuery(Query query, Pageable pageable);

    DictionaryEntry save(DictionaryEntry dictionaryEntry);
    
}
