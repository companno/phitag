package de.garrafao.phitag.domain.dictionary.example;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import de.garrafao.phitag.domain.dictionary.sense.DictionaryEntrySense;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "phitagdictionaryentrysenseexample")
@Getter
@EqualsAndHashCode
@ToString
public class DictionaryEntrySenseExample {

    @EmbeddedId
    private DictionaryEntrySenseExampleId id;

    @MapsId("dictionaryentrysenseid")
    @ManyToOne
    private DictionaryEntrySense dictionaryentrysense;

    @Column(name = "example", nullable = false)
    private String example;

    @Column(name = "order", nullable = false)
    private int order;

    DictionaryEntrySenseExample() {
    }

    public DictionaryEntrySenseExample(final DictionaryEntrySense dictionaryentrysense, final String example,
            final int order) {
        Validate.notNull(dictionaryentrysense, "dictionaryentrysense must not be null");
        Validate.notEmpty(example, "example must not be null");
        Validate.isTrue(order >= 0, "order must be greater than or equal to 0");

        this.id = new DictionaryEntrySenseExampleId(dictionaryentrysense.getId());
        this.dictionaryentrysense = dictionaryentrysense;

        this.example = example;
        this.order = order;
    }
}
