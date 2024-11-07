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
    private readonly Lemma1: string; 
    private readonly Lemma2: string;
    private readonly Group1: string;
    private readonly Group2: string;

    constructor(id: UsePairInstanceId, firstUsage: Usage, secondUsage: Usage, labelSet: Array<string>, nonLabel: string, Lemma1: string, Lemma2: string, Group1: string, Group2: string) {
        this.id = id;
        this.firstusage = firstUsage;
        this.secondusage = secondUsage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
        this.Lemma1 = Lemma1;
        this.Lemma2 = Lemma2;
        this.Group1 = Group1;
        this.Group2 = Group2;
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

    public getLemma1(): string {
        return this.Lemma1;
    }

    public getLemma2(): string {
        return this.Lemma2;
    }

    public getGroup1(): string {
        return this.Group1;
    }

    public getGroup2(): string {
        return this.Group2;
    }

    public static fromDto(dto: UsePairInstanceDto): UsePairInstance {
        return new UsePairInstance(
            UsePairInstanceId.fromDto(dto.id),
            Usage.fromDto(dto.firstusage),
            Usage.fromDto(dto.secondusage),
            dto.labelSet,
            dto.nonLabel,
            Usage.fromDto(dto.firstusage).getLemma(),
            Usage.fromDto(dto.secondusage).getLemma(),
            Usage.fromDto(dto.firstusage).getGroup(),
            Usage.fromDto(dto.secondusage).getGroup(),
            
           
        );
    }
}

export class UsePairInstanceConstructor implements IInstanceConstructor {
    fromDto(dto: UsePairInstanceDto): UsePairInstance {
        return UsePairInstance.fromDto(dto);
    }
} 

