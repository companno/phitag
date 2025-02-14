package de.garrafao.phitag.domain.judgement.usetriplejudgement;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;
import de.garrafao.phitag.domain.judgement.IJudgement;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "phitagusetriplejudgement")
@Getter
public class UseTripleJudgement implements IJudgement {

    @Setter
    @EmbeddedId
    private UseTripleJudgementId id;

    @MapsId("annotatorid")
    @ManyToOne
    private Annotator annotator;

    @MapsId("instanceid")
    @ManyToOne
    private UseTripleInstance instance;

    @Setter
    @Column(name = "label", nullable = false)
    private String label;

    @Setter
    @Column(name = "comment", nullable = false)
    private String comment;

    UseTripleJudgement() {
    }

    public UseTripleJudgement(UseTripleInstance useTripleInstance, Annotator annotator, String label, String comment) {
        Validate.notNull(useTripleInstance, "instanceData must not be null");
        Validate.notNull(annotator, "annotator must not be null");
        Validate.notNull(label, "label must not be null");
        Validate.notNull(comment, "comment must not be null");

        this.id = new UseTripleJudgementId(useTripleInstance.getId(), annotator.getId());

        this.instance = useTripleInstance;
        this.annotator = annotator;

        this.label = label;
        this.comment = comment;
    }

    public UseTripleInstance getUseTripleInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "UseTripleJudgement [id=" + id + ", label=" + label + ", comment=" + comment + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UseTripleJudgement other = (UseTripleJudgement) obj;
        return this.id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

}
