package de.garrafao.phitag.domain.dictionary.entry;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import de.garrafao.phitag.domain.dictionary.dictionary.Dictionary;
import de.garrafao.phitag.domain.dictionary.sense.DictionaryEntrySense;
import de.garrafao.phitag.domain.dictionary.unknownentrydata.DictionaryUnknownEntryData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "phitagdictionaryentry")
@Getter
@EqualsAndHashCode
@ToString
public class DictionaryEntry {

    @EmbeddedId
    private DictionaryEntryId id;

    @MapsId("dictionaryid")
    @ManyToOne
    private Dictionary dictionary;

    @EqualsAndHashCode.Exclude
    @Column(name = "headword", nullable = false)
    private String headword;

    @EqualsAndHashCode.Exclude
    @Column(name = "partofspeech", nullable = false)
    private String partOfSpeech;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "dictionaryentry")
    private List<DictionaryEntrySense> senses;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "dictionaryentry")
    private List<DictionaryUnknownEntryData> unknownentrydata;

    DictionaryEntry() {
    }

    public DictionaryEntry(final Dictionary dictionary, final String headword, final String partOfSpeech) {
        Validate.notNull(dictionary, "dictionary must not be null");
        Validate.notEmpty(headword, "headword must not be null");

        this.id = new DictionaryEntryId(dictionary.getId());
        this.dictionary = dictionary;

        this.headword = headword;
        this.partOfSpeech = partOfSpeech;
    }

}
