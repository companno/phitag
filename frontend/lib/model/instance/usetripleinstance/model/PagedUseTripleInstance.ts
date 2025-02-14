import PagedGeneric from "../../../interfaces/PagedGeneric";
import PagedGenericDto from "../../../interfaces/PagedGenericDto";
import UseTripleInstanceDto from "../dto/UseTripleInstanceDto";
import UseTripleInstance from "./UseTripleInstance";

export default class PagedUseTripleInstance implements PagedGeneric<UseTripleInstance> {
    
        readonly content: UseTripleInstance[];
    
        readonly page: number;
        readonly size: number;
        readonly totalElements: number;
        readonly totalPages: number;
    
        constructor(
            content: UseTripleInstance[],
            
            page: number,
            size: number,
            totalElements: number,
            totalPages: number
        ) {
            this.content = content;
    
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }
    
        public getContent(): UseTripleInstance[] {
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
    
        public static fromDto(dto: PagedGenericDto<UseTripleInstanceDto>): PagedUseTripleInstance {
            return new PagedUseTripleInstance(
                dto.content.map(UseTripleInstance.fromDto),
                dto.page,
                dto.size,
                dto.totalElements,
                dto.totalPages
            );
        }
        
        public static empty(): PagedUseTripleInstance {
            return new PagedUseTripleInstance(
                [],
                0,
                0,
                0,
                0
            );
        }
}
