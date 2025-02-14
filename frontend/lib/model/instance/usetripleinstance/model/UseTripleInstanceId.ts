import { IInstanceId, IInstanceIdConstructor } from "../../model/IInstanceId";
import UseTripleInstanceIdDto from "../dto/UseTripleInstanceIdDto";

export default class UseTripleInstanceId implements IInstanceId {
    
    readonly instanceId: string;

    readonly phase: string;
    readonly project: string;
    readonly owner: string;

    constructor(instanceId: string, phase: string, project: string, owner: string) {
        this.instanceId = instanceId;
        this.phase = phase;
        this.project = project;
        this.owner = owner;
    }

    public getInstanceId(): string {
        return this.instanceId;
    }

    public getPhase(): string {
        return this.phase;
    }

    public getProject(): string {
        return this.project;
    }

    public getOwner(): string {
        return this.owner;
    }

    public static fromDto(dto: UseTripleInstanceIdDto): UseTripleInstanceId {
        return new UseTripleInstanceId(
            dto.instanceId,
            dto.phase,
            dto.project,
            dto.owner
        );
    }

}

export class UseTripleInstanceIdConstructor implements IInstanceIdConstructor {
    fromDto(dto: UseTripleInstanceIdDto): UseTripleInstanceId {
        return UseTripleInstanceId.fromDto(dto);
    }
}
