package de.garrafao.phitag.infrastructure.persistence.jpa.dictionary.dictionary;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.dictionary.dictionary.Dictionary;
import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryRepository;

public class DictionaryRepositoryBridge implements DictionaryRepository {

    private final DictionaryRepositoryJpa dictionaryRepositoryJpa;

    @Autowired
    public DictionaryRepositoryBridge(final DictionaryRepositoryJpa dictionaryRepositoryJpa) {
        this.dictionaryRepositoryJpa = dictionaryRepositoryJpa;
    }

    @Override
    public Page<Dictionary> findByName(String name, PageRequestWraper page) {
        return dictionaryRepositoryJpa.findByName(name, page.getPageRequest());
    }

    @Override
    public Optional<Dictionary> findByIdNameAndIdUname(String name, String uname) {
        return dictionaryRepositoryJpa.findByIdNameAndIdUname(name, uname);
    }

    @Override
    public Dictionary save(Dictionary dictionary) {
        return dictionaryRepositoryJpa.save(dictionary);
    }

}
