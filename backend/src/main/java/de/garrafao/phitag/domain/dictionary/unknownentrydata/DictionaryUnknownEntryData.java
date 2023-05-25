package de.garrafao.phitag.domain.dictionary.unknownentrydata;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import de.garrafao.phitag.domain.dictionary.entry.DictionaryEntry;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name="phitagdictionaryunknownentrydata")
@Getter
@EqualsAndHashCode
@ToString
public class DictionaryUnknownEntryData {

    @EmbeddedId
    private DictionaryUnknownEntryDataId id;

    @MapsId("dictionaryentryid")
    @ManyToOne
    private DictionaryEntry dictionaryentry;

    @EqualsAndHashCode.Exclude
    @Column(name="data", nullable=false)
    private String data;

    @EqualsAndHashCode.Exclude
    @Column(name="order", nullable=false)
    private int order;

    DictionaryUnknownEntryData() {
    }

    public DictionaryUnknownEntryData(final DictionaryEntry dictionaryentry, final String data, final int order) {
        Validate.notNull(dictionaryentry, "dictionaryentry must not be null");
        Validate.notEmpty(data, "data must not be null");
        Validate.isTrue(order >= 0, "order must be greater than or equal to 0");
        
        this.id = new DictionaryUnknownEntryDataId(dictionaryentry.getId());
        this.dictionaryentry = dictionaryentry;

        this.data = data;
        this.order = order;
    }




    
}
