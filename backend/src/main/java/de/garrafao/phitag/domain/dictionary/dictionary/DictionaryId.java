package de.garrafao.phitag.domain.dictionary.dictionary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Embeddable
@EqualsAndHashCode
@ToString
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

}