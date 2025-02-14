package de.garrafao.phitag.application.instance.usetripleinstance.data;

import de.garrafao.phitag.application.instance.data.IInstanceIdDto;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstanceId;
import lombok.Getter;

@Getter
public class UseTripleInstanceIdDto implements IInstanceIdDto {

    private final String instanceId;
    private final String phase;
    private final String project;
    private final String owner;

    private UseTripleInstanceIdDto(final String instanceId, final String phase, final String project,
            final String owner) {
        this.instanceId = instanceId;
        this.phase = phase;
        this.project = project;
        this.owner = owner;
    }

    public static UseTripleInstanceIdDto from(final UseTripleInstanceId useTripleInstanceId) {
        return new UseTripleInstanceIdDto(
                useTripleInstanceId.getInstanceid(),
                useTripleInstanceId.getPhaseid().getName(),
                useTripleInstanceId.getPhaseid().getProjectid().getName(),
                useTripleInstanceId.getPhaseid().getProjectid().getOwnername());
    }

}
