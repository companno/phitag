package de.garrafao.phitag.infrastructure.persistence.jpa.dictionary.dictionary;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import de.garrafao.phitag.domain.dictionary.dictionary.Dictionary;
import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryId;
import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryRepository;

@Repository
public class DictionaryRepositoryBridge implements DictionaryRepository {

    private final DictionaryRepositoryJpa dictionaryRepositoryJpa;

    @Autowired
    public DictionaryRepositoryBridge(final DictionaryRepositoryJpa dictionaryRepositoryJpa) {
        this.dictionaryRepositoryJpa = dictionaryRepositoryJpa;
    }

    @Override
    public Page<Dictionary> findAllByIdName(String name, PageRequest pagerequest) {
        return dictionaryRepositoryJpa.findAllByIdName(name, pagerequest);
    }

    @Override
    public Page<Dictionary> findAllByIdUname(String uname, PageRequest pagerequest) {
        return dictionaryRepositoryJpa.findAllByIdUname(uname, pagerequest);
    }

    @Override
    public Optional<Dictionary> findById(final DictionaryId id) {
        return dictionaryRepositoryJpa.findById(id);
    }

    @Override
    public Dictionary save(Dictionary dictionary) {
        return dictionaryRepositoryJpa.save(dictionary);
    }

}
