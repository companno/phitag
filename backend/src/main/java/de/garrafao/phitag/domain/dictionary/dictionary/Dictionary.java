package de.garrafao.phitag.domain.dictionary.dictionary;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import de.garrafao.phitag.domain.user.User;
import lombok.Getter;

@Entity
@Table(name = "phitagdictionary")
@Getter
public class Dictionary {

    @EmbeddedId
    private DictionaryId id;

    @MapsId("uname")
    @ManyToOne
    @JoinColumn(name = "uname", referencedColumnName = "username")
    private User uname;

    @Column(name = "description", nullable = false)
    private String description;

    Dictionary() {
    }

    public Dictionary(final String name, final User uname, final String description) {
        Validate.notEmpty(name);
        Validate.matchesPattern(name, "^[a-zA-Z0-9-]+$");

        Validate.notNull(uname);

        this.id = new DictionaryId(name, uname.getUsername());
        this.uname = uname;
        this.description = description;
    }

    
}
