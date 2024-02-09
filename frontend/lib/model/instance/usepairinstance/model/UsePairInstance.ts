import Usage from "../../../phitagdata/usage/model/Usage";
import { IInstance, IInstanceConstructor } from "../../model/IInstance";
import UsePairInstanceDto from "../dto/UsePairInstanceDto";
import UsePairInstanceId from "./UsePairInstanceId";

export default class UsePairInstance implements IInstance {

    readonly id: UsePairInstanceId;

    private readonly firstLemma: string;
    private readonly secondLemma: string;

    private readonly firstusage: Usage;
    private readonly secondusage: Usage;

    private readonly labelSet: Array<string>;
    private readonly nonLabel: string;

    constructor(id: UsePairInstanceId, firstLemma: string, secondLemma: string, firstUsage: Usage, secondUsage: Usage, labelSet: Array<string>, nonLabel: string) {
        this.id = id;
        this.firstLemma = firstLemma;
        this.secondLemma = secondLemma;
        this.firstusage = firstUsage;
        this.secondusage = secondUsage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
    }

    public getId(): UsePairInstanceId {
        return this.id;
    }

    public getFirstlemma(): string {
        return this.firstLemma;
    }

    public getSecondlemma(): string {
        return this.secondLemma;
    }

    public getFirstusage(): Usage {
        return this.firstusage;
    }

    public getSecondusage(): Usage {
        return this.secondusage;
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

    public static fromDto(dto: UsePairInstanceDto): UsePairInstance {
        return new UsePairInstance(
            UsePairInstanceId.fromDto(dto.id),
            Usage.fromDto(dto.firstusage).getLemma(),
            Usage.fromDto(dto.secondusage).getLemma(),
            Usage.fromDto(dto.firstusage),
            Usage.fromDto(dto.secondusage),
            dto.labelSet,
            dto.nonLabel
        );
    }
}

export class UsePairInstanceConstructor implements IInstanceConstructor {
    fromDto(dto: UsePairInstanceDto): UsePairInstance {
        return UsePairInstance.fromDto(dto);
    }
} 

