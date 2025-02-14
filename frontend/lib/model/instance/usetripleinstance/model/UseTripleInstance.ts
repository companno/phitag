import Usage from "../../../phitagdata/usage/model/Usage";
import { IInstance, IInstanceConstructor } from "../../model/IInstance";
import UseTripleInstanceDto from "../dto/UseTripleInstanceDto";
import UseTripleInstanceId from "./UseTripleInstanceId";


export default class UseTripleInstance implements IInstance {

    readonly id: UseTripleInstanceId;

    private readonly firstusage: Usage;
    private readonly secondusage: Usage;
    private readonly thirdusage: Usage;

    private readonly labelSet: Array<string>;
    private readonly nonLabel: string;
    private readonly Lemma1: string; 
    private readonly Lemma2: string;
    private readonly Lemma3: string; 
    private readonly Group1: string;
    private readonly Group2: string;
    private readonly Group3: string;

    constructor(id: UseTripleInstanceId, firstUsage: Usage, secondUsage: Usage, thirdUsage: Usage, labelSet: Array<string>, nonLabel: string, Lemma1: string, Lemma2: string, Lemma3: string, Group1: string, Group2: string, Group3: string) {
        this.id = id;
        this.firstusage = firstUsage;
        this.secondusage = secondUsage;
        this.thirdusage = thirdUsage;
        this.labelSet = labelSet;
        this.nonLabel = nonLabel;
        this.Lemma1 = Lemma1;
        this.Lemma2 = Lemma2;
        this.Lemma3 = Lemma3;
        this.Group1 = Group1;
        this.Group2 = Group2;
        this.Group3 = Group3;
    }

    public getId(): UseTripleInstanceId {
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

    public getLemma3(): string {
        return this.Lemma3;
    }

    public getGroup1(): string {
        return this.Group1;
    }

    public getGroup2(): string {
        return this.Group2;
    }

    public getGroup3(): string {
        return this.Group3;
    }

    public static fromDto(dto: UseTripleInstanceDto): UseTripleInstance {
        return new UseTripleInstance(
            UseTripleInstanceId.fromDto(dto.id),
            Usage.fromDto(dto.firstusage),
            Usage.fromDto(dto.secondusage),
            Usage.fromDto(dto.thirdusage),
            dto.labelSet,
            dto.nonLabel,
            Usage.fromDto(dto.firstusage).getLemma(),
            Usage.fromDto(dto.secondusage).getLemma(),
            Usage.fromDto(dto.thirdusage).getLemma(),
            Usage.fromDto(dto.firstusage).getGroup(),
            Usage.fromDto(dto.secondusage).getGroup(),
            Usage.fromDto(dto.thirdusage).getGroup(),
            
           
        );
    }
}

export class UseTripleInstanceConstructor implements IInstanceConstructor {
    fromDto(dto: UseTripleInstanceDto): UseTripleInstance {
        return UseTripleInstance.fromDto(dto);
    }
} 

