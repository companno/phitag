package de.garrafao.phitag.domain.dictionary.dictionary;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DictionaryRepository {

    Page<Dictionary> findAllByIdUname(final String uname, final PageRequest pagerequest);
    
    Page<Dictionary> findAllByIdName(final String name, final PageRequest pagerequest);

    Optional<Dictionary> findById(final DictionaryId id);

    Dictionary save(Dictionary dictionary);
}
