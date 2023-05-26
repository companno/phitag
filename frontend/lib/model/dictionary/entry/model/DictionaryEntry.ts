import DictionaryEntrySense from "../../sense/model/DictionaryEntrySense";
import DictionaryEntryDto from "../dto/DictionaryEntryDto";
import DictionaryEntryId from "./DictionaryEntryId";

export default class DictionaryEntry {

    readonly id: DictionaryEntryId;

    readonly headword: string;
    readonly partofspeech: string;

    readonly senses: Array<DictionaryEntrySense>;

    constructor(id: DictionaryEntryId, headword: string, partofspeech: string, senses: Array<DictionaryEntrySense>) {
        this.id = id;
        this.headword = headword;
        this.partofspeech = partofspeech;
        this.senses = senses;
    }

    public static fromDto(dto: DictionaryEntryDto) {
        return new DictionaryEntry(
            DictionaryEntryId.fromDto(dto.id),
            dto.headword,
            dto.partofspeech,
            dto.senses.map(DictionaryEntrySense.fromDto)
        );
    }

}