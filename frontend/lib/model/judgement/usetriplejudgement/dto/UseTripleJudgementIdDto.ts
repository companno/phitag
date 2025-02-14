import IJudgementIdDto from "../../dto/IJudgementIdDto";

export default interface UseTripleJudgementIdDto extends IJudgementIdDto {
    readonly id: string;
    readonly instanceId: string;
    readonly annotator: string;

    readonly phase: string;
    readonly project: string;
    readonly owner: string;
}
