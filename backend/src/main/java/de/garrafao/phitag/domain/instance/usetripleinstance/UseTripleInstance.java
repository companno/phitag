package de.garrafao.phitag.domain.instance.usetripleinstance;

import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

import de.garrafao.phitag.domain.instance.IInstance;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phitagdata.usage.Usage;
import lombok.Getter;

@Entity
@Table(name = "phitagusetripleinstance")
@Getter
public class UseTripleInstance implements IInstance {

    @EmbeddedId
    private UseTripleInstanceId id;

    @MapsId("phaseid")
    @ManyToOne
    @JoinColumns({ 
        @JoinColumn(name = "phasename", referencedColumnName = "name"),
        @JoinColumn(name = "projectname", referencedColumnName = "projectname"),
        @JoinColumn(name = "ownername", referencedColumnName = "ownername") 
    })
    private Phase phase;

    @ManyToOne
    @JoinColumns({ 
        @JoinColumn(name = "phitagusage_first_dataid", referencedColumnName = "dataid"),
        @JoinColumn(name = "phitagusage_first_projectname",  referencedColumnName = "projectname"),
        @JoinColumn(name = "phitagusage_first_ownername", referencedColumnName = "ownername") 
    })
    private Usage firstusage;

    @ManyToOne
    @JoinColumns({ 
        @JoinColumn(name = "phitagusage_second_dataid", referencedColumnName = "dataid"),
        @JoinColumn(name = "phitagusage_second_projectname", referencedColumnName = "projectname"),
        @JoinColumn(name = "phitagusage_second_ownername", referencedColumnName = "ownername") 
    })
    private Usage secondusage;

    @ManyToOne
    @JoinColumns({ 
        @JoinColumn(name = "phitagusage_third_dataid", referencedColumnName = "dataid"),
        @JoinColumn(name = "phitagusage_third_projectname", referencedColumnName = "projectname"),
        @JoinColumn(name = "phitagusage_third_ownername", referencedColumnName = "ownername") 
    })
    private Usage thirdusage;

    @Column(name = "label_set", nullable = false)
    private String labelSet;

    @Column(name = "non_label", nullable = false)
    private String nonLabel;

    UseTripleInstance() {
    }

    public UseTripleInstance(final String instanceId, final Phase phase, final Usage firstusage, final Usage secondusage, final Usage thirdusage,
            final String labelSet, final String nonLabel) {
        Validate.notNull(instanceId);
        Validate.notNull(phase);

        Validate.notNull(firstusage);
        Validate.notNull(secondusage);
        Validate.notNull(thirdusage);

        Validate.notNull(labelSet);
        Validate.notEmpty(labelSet);
        Validate.notNull(nonLabel);
        Validate.notEmpty(nonLabel);

        this.phase = phase;
        this.id = new UseTripleInstanceId(instanceId, phase.getId());

        this.firstusage = firstusage;
        this.secondusage = secondusage;
        this.thirdusage = thirdusage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
    }

    public List<String> getLabelSet() {
        return Arrays.asList(labelSet.split(","));
    }

    @Override
    public String toString() {
        return String.format("InstanceData [id=%s]", id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UseTripleInstance other = (UseTripleInstance) obj;
        return this.id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
