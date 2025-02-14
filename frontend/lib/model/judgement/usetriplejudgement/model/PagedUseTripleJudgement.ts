import PagedGeneric from "../../../interfaces/PagedGeneric";
import UseTripleJudgementDto from "../dto/UseTripleJudgementDto";
import UseTripleJudgement from "./UseTripleJudgement";

export default class PagedUseTripleJudgement implements PagedGeneric<UseTripleJudgement> {
    readonly content: UseTripleJudgement[];

    readonly page: number;
    readonly size: number;
    readonly totalElements: number;
    readonly totalPages: number;

    constructor(content: UseTripleJudgement[], page: number, size: number, totalElements: number, totalPages: number) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public getContent(): UseTripleJudgement[] {
        return this.content;
    }

    public getPage(): number {
        return this.page;
    }

    public getSize(): number {
        return this.size;
    }

    public getTotalElements(): number {
        return this.totalElements;
    }

    public getTotalPages(): number {
        return this.totalPages;
    }

    static fromDto(dto: PagedGeneric<UseTripleJudgementDto>): PagedUseTripleJudgement {
        return new PagedUseTripleJudgement(dto.content.map(UseTripleJudgement.fromDto), dto.page, dto.size, dto.totalElements, dto.totalPages);
    }

    static empty(): PagedUseTripleJudgement {
        return new PagedUseTripleJudgement([], 0, 0, 0, 0);
    }

}
