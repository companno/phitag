import DictionaryEntryIdDto from "../dto/DictionaryEntryIdDto";

export default class DictionaryEntryId {

    readonly id: string;
    readonly dname: string;
    readonly uname: string;

    constructor(id: string, dname: string, uname: string) {
        this.id = id;
        this.dname = dname;
        this.uname = uname;
    }

    static fromDto(dto: DictionaryEntryIdDto) {
        return new DictionaryEntryId(
            dto.id,
            dto.dname,
            dto.uname
        );
    }
}