package de.garrafao.phitag.application.instance.usetripleinstance.data;

import java.util.List;

import de.garrafao.phitag.application.instance.data.IInstanceDto;
import de.garrafao.phitag.application.phitagdata.usage.data.UsageDto;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;
import lombok.Getter;

@Getter
public class UseTripleInstanceDto implements IInstanceDto {

    private final UseTripleInstanceIdDto id;

    private final UsageDto firstusage;
    private final UsageDto secondusage;
    private final UsageDto thirdusage;

    private final List<String> labelSet;
    private final String nonLabel;

    private UseTripleInstanceDto(
            final UseTripleInstanceIdDto id,
            final UsageDto firstusage,
            final UsageDto secondusage,
            final UsageDto thirdusage,
            final List<String> labelSet,
            final String nonLabel) {
        this.id = id;

        this.firstusage = firstusage;
        this.secondusage = secondusage;
        this.thirdusage = thirdusage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
    }

    public static UseTripleInstanceDto from(final UseTripleInstance useTripleInstance) {
        if (useTripleInstance == null) {
            return null;
        }

        return new UseTripleInstanceDto(
                UseTripleInstanceIdDto.from(useTripleInstance.getId()),
                UsageDto.from(useTripleInstance.getFirstusage()),
                UsageDto.from(useTripleInstance.getSecondusage()),
                UsageDto.from(useTripleInstance.getThirdusage()),
                useTripleInstance.getLabelSet(),
                useTripleInstance.getNonLabel());
    }
}
