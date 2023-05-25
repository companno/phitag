package de.garrafao.phitag.domain.dictionary.dictionary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class DictionaryId implements Serializable {

    @Column(name = "name")
    private String name;

    private String uname;

    public DictionaryId() {
    }

    public DictionaryId(final String name, final String uname) {
        this.name = name;
        this.uname = uname;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uname == null) ? 0 : uname.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        DictionaryId other = (DictionaryId) obj;
        return name.equals(other.name) && uname.equals(other.uname);
    }

    @Override
    public String toString() {
        return String.format("DictionaryId[name='%s', uname='%s']", name, uname);
    }

}