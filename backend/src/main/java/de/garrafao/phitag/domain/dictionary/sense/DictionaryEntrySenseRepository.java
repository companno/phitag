package de.garrafao.phitag.domain.dictionary.sense;

import java.util.Optional;

public interface DictionaryEntrySenseRepository {

    Optional<DictionaryEntrySense> findById(final DictionaryEntrySenseId id);

    DictionaryEntrySense save(DictionaryEntrySense sense);

    void delete(DictionaryEntrySense sense);

}