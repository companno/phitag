package de.garrafao.phitag.domain.instance.usetripleinstance;

import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phitagdata.usage.Usage;
import lombok.Data;

public class UseTripleInstanceFactory {

    private String instanceId;
    private Phase phase;

    private Usage firstUsage;
    private Usage secondUsage;
    private Usage thirdUsage;

    private String labelSet;
    private String nonLabel;

    public UseTripleInstanceFactory withInstanceId(final String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public UseTripleInstanceFactory withPhase(final Phase phase) {
        this.phase = phase;
        return this;
    }

    public UseTripleInstanceFactory withFirstUsage(final Usage firstUsage) {
        this.firstUsage = firstUsage;
        return this;
    }

    public UseTripleInstanceFactory withSecondUsage(final Usage secondUsage) {
        this.secondUsage = secondUsage;
        return this;
    }

    public UseTripleInstanceFactory withThirdUsage(final Usage thirdUsage) {
        this.thirdUsage = thirdUsage;
        return this;
    }

    public UseTripleInstanceFactory withLabelSet(final String labelSet) {
        this.labelSet = labelSet;
        return this;
    }

    public UseTripleInstanceFactory withNonLabel(final String nonLabel) {
        this.nonLabel = nonLabel;
        return this;
    }

    public UseTripleInstance build() {
        return new UseTripleInstance(instanceId, phase, firstUsage, secondUsage, thirdUsage, labelSet, nonLabel);
    }

}
