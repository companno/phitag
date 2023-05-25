package de.garrafao.phitag.domain.dictionary.entry;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryId;
import lombok.Data;

@Data
@Embeddable
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
                + ((dictionaryid == null) ? 0 : dictionaryid.hashCode());
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        DictionaryEntryId other = (DictionaryEntryId) obj;
        return dictionaryid.equals(other.dictionaryid) && id.equals(other.id);
    }

    @Override
    public String toString() {
        return String.format("DictionaryEntryId[id='%s', dictionaryId='%s']",
                id, dictionaryid);
    }

}
