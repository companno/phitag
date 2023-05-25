package de.garrafao.phitag.domain.dictionary.sense;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.garrafao.phitag.domain.dictionary.entry.DictionaryEntryId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Embeddable
@EqualsAndHashCode
@ToString
public class DictionaryEntrySenseId implements Serializable {

    @Column(name = "id")
    private String id;

    private DictionaryEntryId dictionaryentryid;

    public DictionaryEntrySenseId() {
    }

    public DictionaryEntrySenseId(final DictionaryEntryId dictionaryentryid) {
        this.id = UUID.randomUUID().toString();
        this.dictionaryentryid = dictionaryentryid;
    }
    
}
