import JoblistingIdDto from "./JoblistingIdDto";

export default interface PersonalJoblistingDto {
    
    readonly id: JoblistingIdDto;

    readonly displayname: string;

    readonly open: boolean;
    readonly description: string;

    readonly waitinglist: Array<string>;
}