import UsageDto from "../../../phitagdata/usage/dto/UsageDto";
import IInstanceDto from "../../dto/IInstanceDto";
import UseTripleInstanceIdDto from "./UseTripleInstanceIdDto";

export default interface UseTripleInstanceDto extends IInstanceDto {
    group: string;
    lemma: string;

    readonly id: UseTripleInstanceIdDto;

    readonly firstusage: UsageDto;
    readonly secondusage: UsageDto;
    readonly thirdusage: UsageDto;

    readonly labelSet: Array<string>;
    readonly nonLabel: string;
}
