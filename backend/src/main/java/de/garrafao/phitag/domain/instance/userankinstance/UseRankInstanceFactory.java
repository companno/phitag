package de.garrafao.phitag.domain.instance.userankinstance;

import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phitagdata.usage.Usage;

public class UseRankInstanceFactory {

    private String instanceId;

    private Phase phase;

    private Usage firstUsage;
    private Usage secondUsage;
    private Usage thirdUsage;
    private Usage fourthUsage;

    private String labelSet;

    private String nonLabel;

    public UseRankInstanceFactory withInstanceId(final String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public UseRankInstanceFactory withPhase(final Phase phase) {
        this.phase = phase;
        return this;
    }

    public UseRankInstanceFactory withFirstUsage(final Usage firstUsage) {
        this.firstUsage = firstUsage;
        return this;
    }

    public UseRankInstanceFactory withSecondUsage(final Usage secondUsage) {
        this.secondUsage = secondUsage;
        return this;
    }

    public UseRankInstanceFactory withThirdUsage(final Usage thirdUsage) {
        this.thirdUsage = thirdUsage;
        return this;
    }
    public UseRankInstanceFactory withFourthUsage(final Usage fourthUsage) {
        this.fourthUsage = fourthUsage;
        return this;
    }


    public UseRankInstanceFactory withLabelSet(final String labelSet) {
        this.labelSet = labelSet;
        return this;
    }

    public UseRankInstanceFactory withNonLabel(final String nonLabel) {
        this.nonLabel = nonLabel;
        return this;
    }

    public UseRankInstance build() {
        return new UseRankInstance(instanceId, phase, firstUsage, secondUsage, thirdUsage, fourthUsage, labelSet, nonLabel);
    }

}
