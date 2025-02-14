package de.garrafao.phitag.domain.judgement.usetriplejudgement;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import de.garrafao.phitag.domain.annotator.AnnotatorId;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstanceId;
import de.garrafao.phitag.domain.judgement.IJudgementId;
import lombok.Data;

@Data
@Embeddable
public class UseTripleJudgementId implements Serializable, IJudgementId {

    @Column(name = "uuid")
    private String uuid;

    private AnnotatorId annotatorid;

    private UseTripleInstanceId instanceid;

    public UseTripleJudgementId() {
    }

    public UseTripleJudgementId(final UseTripleInstanceId instanceid, final AnnotatorId annotatorid) {
        this.uuid = UUID.randomUUID().toString();
        this.instanceid = instanceid;
        this.annotatorid = annotatorid;
    }

    @Override
    public String toString() {
        return String.format("UseTripleJudgementId [instanceid=%s, annotatorid=%s]", instanceid, annotatorid);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annotatorid == null) ? 0 : annotatorid.hashCode());
        result = prime * result + ((instanceid == null) ? 0 : instanceid.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UseTripleJudgementId other = (UseTripleJudgementId) obj;
        return uuid.equals(other.uuid) && annotatorid.equals(other.annotatorid)
                && instanceid.equals(other.instanceid);
    }

    @Override
    public String getUUID() {
        // TODO Auto-generated method stub
        return uuid;
    }

}
