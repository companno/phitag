package de.garrafao.phitag.application.judgement.usetriplejudgement.data;

import de.garrafao.phitag.application.judgement.data.IJudgementIdDto;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgementId;
import lombok.Getter;

@Getter
public class UseTripleJudgementIdDto implements IJudgementIdDto {

    private final String id;
    private final String instanceId;
    private final String annotator;
    private final String phase;
    private final String project;
    private final String owner;

    private UseTripleJudgementIdDto(
            final String id,
            final String instanceId, final String annotator,
            final String phase, final String project, final String owner) {
        this.id = id;

        this.instanceId = instanceId;
        this.annotator = annotator;

        this.phase = phase;
        this.project = project;
        this.owner = owner;
    }

    public static UseTripleJudgementIdDto from(final UseTripleJudgementId useTripleJudgementId) {
        return new UseTripleJudgementIdDto(
                useTripleJudgementId.getUUID(),
                useTripleJudgementId.getInstanceid().getInstanceid(),
                useTripleJudgementId.getAnnotatorid().getUsername(),
                useTripleJudgementId.getInstanceid().getPhaseid().getName(),
                useTripleJudgementId.getInstanceid().getPhaseid().getProjectid().getName(),
                useTripleJudgementId.getInstanceid().getPhaseid().getProjectid().getOwnername());
    }

}
