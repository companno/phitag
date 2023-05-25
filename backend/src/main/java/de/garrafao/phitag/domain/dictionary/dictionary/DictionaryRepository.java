package de.garrafao.phitag.domain.dictionary.dictionary;

import java.util.Optional;

import org.springframework.data.domain.Page;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;

public interface DictionaryRepository {
    
    // Page<Dictionary> findByQueryPaged(Query query, PageRequestWraper page);

    Page<Dictionary> findByName(final String name, final PageRequestWraper page);

    Optional<Dictionary> findByIdNameAndIdUname(String name, String uname);

    Dictionary save(Dictionary dictionary);
}
