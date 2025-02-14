import UseTripleInstance from "../../../instance/usetripleinstance/model/UseTripleInstance";
import { IJudgement, IJudgementConstructor } from "../../model/IJudgement";
import UseTripleJudgementDto from "../dto/UseTripleJudgementDto";
import UseTripleJudgementId from "./UseTripleJudgementId";

export default class UseTripleJudgement implements IJudgement {

    readonly id: UseTripleJudgementId;

    readonly instance: UseTripleInstance;
    
    readonly label: string;
    readonly comment: string;

    constructor(id: UseTripleJudgementId, instance: UseTripleInstance, label: string, comment: string) {
        this.id = id;
        this.instance = instance;
        this.label = label;
        this.comment = comment;
    }

    public getId(): UseTripleJudgementId {
        return this.id;
    }

    public getInstance(): UseTripleInstance {
        return this.instance;
    }

    public getLabel(): string {
        return this.label;
    }

    public getComment(): string {
        return this.comment;
    }

    public static fromDto(dto: UseTripleJudgementDto): UseTripleJudgement {
        return new UseTripleJudgement(
            UseTripleJudgementId.fromDto(dto.id),
            UseTripleInstance.fromDto(dto.instance),
            dto.label,
            dto.comment
        );
    }

}

export class UseTripleJudgementConstructor implements IJudgementConstructor {
    fromDto(dto: UseTripleJudgementDto): UseTripleJudgement {
        return UseTripleJudgement.fromDto(dto);
    }
}
