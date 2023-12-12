import Usage from "../../../phitagdata/usage/model/Usage";
import { IInstance, IInstanceConstructor } from "../../model/IInstance";
import UseRankInstanceDto from "../dto/UseRankInstanceDto";
import UseRankInstanceId from "./UseRankInstanceId";

export default class UseRankInstance implements IInstance {

    readonly id: UseRankInstanceId;

    private readonly firstusage: Usage;
    private readonly secondusage: Usage;
    private readonly thirdusage: Usage;
    private readonly fourthusage: Usage;

    private readonly labelSet: Array<string>;
    private readonly nonLabel: string;

    constructor(id: UseRankInstanceId, firstUsage: Usage, secondUsage: Usage,
        thirdusage: Usage, fourthusage: Usage,
        labelSet: Array<string>, nonLabel: string) {
        this.id = id;
        this.firstusage = firstUsage;
        this.secondusage = secondUsage;
        this.thirdusage = thirdusage;
        this.fourthusage = fourthusage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
    }

    public getId(): UseRankInstanceId {
        return this.id;
    }

    public getFirstusage(): Usage {
        return this.firstusage;
    }

    public getSecondusage(): Usage {
        return this.secondusage;
    }
    public getThirdusage(): Usage {
        return this.thirdusage;
    }

    public getFourthusage(): Usage {
        return this.fourthusage;
    }

    public getLabelSet(): Array<string> {
        return this.labelSet;
    }

    public getNonLabel(): string {
        return this.nonLabel;
    }

    public getLabelAndNonLabel(): Array<string> {
        return this.labelSet.concat(this.nonLabel);
    }

    public static fromDto(dto: UseRankInstanceDto): UseRankInstance {
        return new UseRankInstance(
            UseRankInstanceId.fromDto(dto.id),
            Usage.fromDto(dto.firstusage),
            Usage.fromDto(dto.secondusage),
            Usage.fromDto(dto.thirdusage),
            Usage.fromDto(dto.fourthusage),
            dto.labelSet,
            dto.nonLabel
        );
    }
}

export class UseRankInstanceConstructor implements IInstanceConstructor {
    fromDto(dto: UseRankInstanceDto): UseRankInstance {
        return UseRankInstance.fromDto(dto);
    }
} 

