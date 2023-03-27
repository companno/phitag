import LanguageDto from "../../language/dto/LanguageDto";
import VisibilityDto from "../../visibility/dto/VisibilityDto";

export default interface UserDataDto {
    readonly username: string;
    readonly displayname: string;
    readonly email: string;
    
    readonly enabled: boolean;
    
    readonly visibility: VisibilityDto;
    readonly languages: Array<LanguageDto>;
    readonly description: string;
}
