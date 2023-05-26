import DictionaryEntrySenseExampleDto from "../dto/DictionaryEntrySenseExampleDto";
import DictionaryEntrySenseExampleId from "./DictionaryEntrySenseExampleId";

export default class DictionaryEntrySenseExample {

    readonly id: DictionaryEntrySenseExampleId;

    readonly example: string;
    readonly order: number;

    constructor(id: DictionaryEntrySenseExampleId, example: string, order: number) {
        this.id = id;
        this.example = example;
        this.order = order;
    }

    public static fromDto(dto: DictionaryEntrySenseExampleDto) {
        return new DictionaryEntrySenseExample(
            DictionaryEntrySenseExampleId.fromDto(dto.id),
            dto.example,
            dto.order
        );
    }
}