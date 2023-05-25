package de.garrafao.phitag.domain.dictionary.sense;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import de.garrafao.phitag.domain.dictionary.entry.DictionaryEntry;
import de.garrafao.phitag.domain.dictionary.example.DictionaryEntrySenseExample;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name="phitagdictionaryentrysense")
@Getter
@EqualsAndHashCode
@ToString
public class DictionaryEntrySense {

    @EmbeddedId
    private DictionaryEntrySenseId id;

    @MapsId("dictionaryentryid")
    @ManyToOne
    private DictionaryEntry dictionaryentry;

    @Column(name="definition", nullable=false)
    private String definition;

    @Column(name="order", nullable=false)
    private int order;

    @OneToMany(mappedBy="dictionaryentrysense")
    private DictionaryEntrySenseExample examples;

    DictionaryEntrySense() {
    }

    public DictionaryEntrySense(final DictionaryEntry dictionaryentry, final String definition, final int order) {
        Validate.notNull(dictionaryentry, "dictionaryentry must not be null");
        Validate.notEmpty(definition, "definition must not be null");
        Validate.isTrue(order >= 0, "order must be greater than or equal to 0");
        
        this.id = new DictionaryEntrySenseId(dictionaryentry.getId());
        this.dictionaryentry = dictionaryentry;

        this.definition = definition;
        this.order = order;
    }
    
}
