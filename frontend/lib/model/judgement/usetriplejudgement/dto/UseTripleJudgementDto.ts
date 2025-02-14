import UseTripleInstanceDto from "../../../instance/usetripleinstance/dto/UseTripleInstanceDto";
import IJudgementDto from "../../dto/IJudgementDto";
import UseTripleJudgementIdDto from "./UseTripleJudgementIdDto";

export default interface UseTripleJudgementDto extends IJudgementDto {

    readonly id: UseTripleJudgementIdDto;

    readonly instance: UseTripleInstanceDto;
    
    readonly label: string;
    readonly comment: string;
}
