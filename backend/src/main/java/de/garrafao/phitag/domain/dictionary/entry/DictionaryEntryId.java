package de.garrafao.phitag.domain.dictionary.entry;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Embeddable
@EqualsAndHashCode
@ToString
public class DictionaryEntryId implements Serializable {

    @Column(name = "id")
    private String id;

    private DictionaryId dictionaryid;

    public DictionaryEntryId() {
    }

    public DictionaryEntryId(final DictionaryId dictionaryid) {
        this.id = UUID.randomUUID().toString();
        this.dictionaryid = dictionaryid;
    }

    public DictionaryEntryId(final String id, final DictionaryId dictionaryid) {
        this.id = id;
        this.dictionaryid = dictionaryid;
    }

}
