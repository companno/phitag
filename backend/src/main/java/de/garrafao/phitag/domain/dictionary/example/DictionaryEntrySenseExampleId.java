package de.garrafao.phitag.domain.dictionary.example;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.garrafao.phitag.domain.dictionary.sense.DictionaryEntrySenseId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Embeddable
@EqualsAndHashCode
@ToString
public class DictionaryEntrySenseExampleId implements Serializable {
    
    @Column(name = "id")
    private String id;

    private DictionaryEntrySenseId dictionaryentrysenseid;

    public DictionaryEntrySenseExampleId() {
    }

    public DictionaryEntrySenseExampleId(final DictionaryEntrySenseId dictionaryentrysenseid) {
        this.id = UUID.randomUUID().toString();
        this.dictionaryentrysenseid = dictionaryentrysenseid;
    }

    public DictionaryEntrySenseExampleId(final String id, final DictionaryEntrySenseId dictionaryentrysenseid) {
        this.id = id;
        this.dictionaryentrysenseid = dictionaryentrysenseid;
    }
    
}
