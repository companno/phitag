package de.garrafao.phitag.application.judgement.usetriplejudgement.data;

import de.garrafao.phitag.application.instance.usetripleinstance.data.UseTripleInstanceDto;
import de.garrafao.phitag.application.judgement.data.IJudgementDto;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgement;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class UseTripleJudgementDto implements IJudgementDto {

    private final UseTripleJudgementIdDto id;

    private final UseTripleInstanceDto instance;

    private final String label;
    private final String comment;

    private UseTripleJudgementDto(
            final UseTripleJudgementIdDto id,
            final UseTripleInstanceDto instance,
            final String label,
            final String comment) {
        this.id = id;
        this.instance = instance;
        this.label = label;
        this.comment = comment;
    }

    public static UseTripleJudgementDto from(@NonNull final UseTripleJudgement useTripleJudgement) {
        return new UseTripleJudgementDto(
                UseTripleJudgementIdDto.from(useTripleJudgement.getId()),
                UseTripleInstanceDto.from(useTripleJudgement.getUseTripleInstance()),
                useTripleJudgement.getLabel(),
                useTripleJudgement.getComment());
    }
}
