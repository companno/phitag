package de.garrafao.phitag.domain.dictionary.entry.query;

import de.garrafao.phitag.domain.core.QueryComponent;
import lombok.Getter;

@Getter
public class DictionaryNameQueryComponent implements QueryComponent {

    private final String name;

    public DictionaryNameQueryComponent(String name) {
        this.name = name;
    }
    
}
