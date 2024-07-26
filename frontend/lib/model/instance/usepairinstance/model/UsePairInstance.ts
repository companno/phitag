import Usage from "../../../phitagdata/usage/model/Usage";
import { IInstance, IInstanceConstructor } from "../../model/IInstance";
import UsePairInstanceDto from "../dto/UsePairInstanceDto";
import UsePairInstanceId from "./UsePairInstanceId";

export default class UsePairInstance implements IInstance {

    readonly id: UsePairInstanceId;

    private readonly firstusage: Usage;
    private readonly secondusage: Usage;

    private readonly labelSet: Array<string>;
    private readonly nonLabel: string;
    private readonly lemma: string; 
    private readonly group: string;

    constructor(id: UsePairInstanceId, firstUsage: Usage, secondUsage: Usage, labelSet: Array<string>, nonLabel: string, lemma: string, group: string) {
        this.id = id;
        this.firstusage = firstUsage;
        this.secondusage = secondUsage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
        this.lemma = lemma;
        this.group = group;
    }

    public getId(): UsePairInstanceId {
        return this.id;
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

    public getlemma(): string {
        return this.lemma;
    }

    public getgroup(): string {
        return this.group;
    }

    public static fromDto(dto: UsePairInstanceDto): UsePairInstance {
        return new UsePairInstance(
            UsePairInstanceId.fromDto(dto.id),
            Usage.fromDto(dto.firstusage),
            Usage.fromDto(dto.secondusage),
            dto.labelSet,
            dto.nonLabel,
            dto.lemma,
            dto.group
        );
    }
}

export class UsePairInstanceConstructor implements IInstanceConstructor {
    fromDto(dto: UsePairInstanceDto): UsePairInstance {
        return UsePairInstance.fromDto(dto);
    }
} 

