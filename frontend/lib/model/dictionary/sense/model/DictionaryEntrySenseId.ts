import DictionaryEntrySenseIdDto from "../dto/DictionaryEntrySenseIdDto";

export default class DictionaryEntrySenseId {
    readonly id: string;
    readonly entryId: string;
    readonly dname: string;
    readonly uname: string;

    constructor(id: string, entryId: string, dname: string, uname: string) {
        this.id = id;
        this.entryId = entryId;
        this.dname = dname;
        this.uname = uname;
    }

    static fromDto(dto: DictionaryEntrySenseIdDto) {
        return new DictionaryEntrySenseId(
            dto.id,
            dto.entryId,
            dto.dname,
            dto.uname
        );
    }
}
