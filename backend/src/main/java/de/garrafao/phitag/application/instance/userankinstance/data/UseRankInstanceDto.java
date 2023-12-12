package de.garrafao.phitag.application.instance.userankinstance.data;

import de.garrafao.phitag.application.instance.data.IInstanceDto;
import de.garrafao.phitag.application.phitagdata.usage.data.UsageDto;
import de.garrafao.phitag.domain.instance.userankinstance.UseRankInstance;
import lombok.Getter;

import java.util.List;

@Getter
public class UseRankInstanceDto implements IInstanceDto {

    private final UseRankInstanceIdDto id;

    private final UsageDto firstusage;
    private final UsageDto secondusage;
    private final UsageDto thirdusage;
    private final UsageDto fourthusage;

    private final List<String> labelSet;
    private final String nonLabel;

    private UseRankInstanceDto(
            final UseRankInstanceIdDto id,
            final UsageDto firstusage,
            final UsageDto secondusage,
            final UsageDto thirdusage,
            final UsageDto fourthusage,
            final List<String> labelSet,
            final String nonLabel) {
        this.id = id;

        this.firstusage = firstusage;
        this.secondusage = secondusage;
        this.thirdusage = thirdusage;
        this.fourthusage = fourthusage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
    }

    public static UseRankInstanceDto from(final UseRankInstance useRankInstance) {
        if (useRankInstance == null) {
            return null;
        }

        return new UseRankInstanceDto(
                UseRankInstanceIdDto.from(useRankInstance.getId()),
                UsageDto.from(useRankInstance.getFirstusage()),
                UsageDto.from(useRankInstance.getSecondusage()),
                UsageDto.from(useRankInstance.getThirdusage()),
                UsageDto.from(useRankInstance.getFourthusage()),
                useRankInstance.getLabelSet(),
                useRankInstance.getNonLabel());
    }
}
