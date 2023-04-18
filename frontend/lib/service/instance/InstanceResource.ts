import axios from "axios";
import useSWR from "swr";
import fileDownload from "js-file-download";

// Custom Hooks
import useStorage from "../../hook/useStorage";

// Routes
import BACKENDROUTES from "../../BackendRoutes";

// Interface for the return value of the custom hook
import IInstanceDto from "../../model/instance/dto/IInstanceDto";
import { IInstance, IInstanceConstructor } from "../../model/instance/model/IInstance";
import WSSIMTagDto from "../../model/instance/wssimtag/dto/WSSIMTagDto";
import WSSIMTag from "../../model/instance/wssimtag/model/WSSIMTag";
import PagedGenericDto from "../../model/interfaces/PagedGenericDto";
import PagedGeneric from "../../model/interfaces/PagedGeneric";
import PagedUsePairInstance from "../../model/instance/usepairinstance/model/PagedUsePairInstance";
import UsePairInstanceDto from "../../model/instance/usepairinstance/dto/UsePairInstanceDto";
import WSSIMInstanceDto from "../../model/instance/wssiminstance/dto/WSSIMInstanceDto";
import PagedWSSIMInstance from "../../model/instance/wssiminstance/model/PagedWSSIMInstance";
import PagedWSSIMTag from "../../model/instance/wssimtag/model/PagedWSSIMTag";
import LexSubInstanceDto from "../../model/instance/lexsubinstance/dto/LexSubInstanceDto";
import PagedLexSubInstance from "../../model/instance/lexsubinstance/model/PagedLexSubInstance";

/**
 * Returns all instances of a phase
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param constructor data class to be converted to (implements fromDto, i.e. the convertion function)
 * @param additional if additional data should be fetched (e.g. wssim )
 * @param fetch if data should be fetched
 * @returns list of all instances
 */
export function useFetchInstances<G extends IInstance, T extends IInstanceConstructor>(owner: string, project: string, phase: string, constructor: T, additional: boolean = false, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<IInstanceDto[]>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.INSTANCE}?owner=${owner}&project=${project}&phase=${phase}&additional=${additional}` : null, queryPhaseDataFetcher)

    return {
        data: data ? data.map(constructor.fromDto) : [].map(constructor.fromDto) as G[],
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/**
 * Returns all use pair instances of a phase as a page.
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param constructor data class to be converted to (implements fromDto, i.e. the convertion function)
 * @param additional if additional data should be fetched (e.g. wssim )
 * 
 * @param page page number
 * @param fetch if data should be fetched
 * @returns paged list of all instances
 */
export function useFetchPagedUsePairInstance(owner: string, project: string, phase: string, page: number = 0, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<UsePairInstanceDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.INSTANCE}/paged?owner=${owner}&project=${project}&phase=${phase}&additional=${false}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedUsePairInstance.fromDto(data) : PagedUsePairInstance.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/**
 * Returns all WSSIM instances of a phase as a page.
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param constructor data class to be converted to (implements fromDto, i.e. the convertion function)
 * @param additional if additional data should be fetched (e.g. wssim )
 * 
 * @param page page number
 * @param fetch if data should be fetched
 * @returns paged list of all instances
 */
export function useFetchPagedWSSIMInstance(owner: string, project: string, phase: string, page: number = 0, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<WSSIMInstanceDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.INSTANCE}/paged?owner=${owner}&project=${project}&phase=${phase}&additional=${false}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedWSSIMInstance.fromDto(data) : PagedWSSIMInstance.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/**
 * Returns all uwssimtag of a phase as a page.
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param constructor data class to be converted to (implements fromDto, i.e. the convertion function)
 * @param additional if additional data should be fetched (e.g. wssim )
 * 
 * @param page page number
 * @param fetch if data should be fetched
 * @returns paged list of all instances
 */
export function useFetchPagedWSSIMTAG(owner: string, project: string, phase: string, page: number = 0, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<WSSIMTagDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.INSTANCE}/paged?owner=${owner}&project=${project}&phase=${phase}&additional=${true}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedWSSIMTag.fromDto(data) : PagedWSSIMTag.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/**
 * Returns all lexsub instances of a phase as a page.
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * 
 * @param page page number
 * @param fetch if data should be fetched
 */
export function useFetchPagedLexSubInstance(owner: string, project: string, phase: string, page: number = 0, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<LexSubInstanceDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.INSTANCE}/paged?owner=${owner}&project=${project}&phase=${phase}&additional=${false}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedLexSubInstance.fromDto(data) : PagedLexSubInstance.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}




/**
 * Fetches a instance of a phase without revalidation
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param constructor data class to be converted to (implements fromDto, i.e. the convertion function)
 * @param get storage hook
 * @returns a specific instance without revalidation
 */
export function fetchRandomInstance<G extends IInstance, T extends IInstanceConstructor>(owner: string, project: string, phase: string, constructor: T, get: Function = () => {}) {
    const token = get('JWT') ?? '';

    return axios.get<IInstanceDto>(`${BACKENDROUTES.INSTANCE}/random?owner=${owner}&project=${project}&phase=${phase}`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => constructor.fromDto(res.data) as G)
}

/**
 * Exports all instances of a phase as a csv/tsv file
 * 
 * @param owner owner of the project
 * @param project project name 
 * @param phase phase name in the project
 * @param get storage hook
 * @returns a csv/tsv file
 */
export function exportInstance(owner: string, project: string, phase: string, additional: boolean = false, get: Function = () => {}) {
    const token = get('JWT') ?? '';

    return axios.get(`${BACKENDROUTES.INSTANCE}/export?owner=${owner}&project=${project}&phase=${phase}&additional=${additional}`, {
        responseType: 'blob',
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => {
        fileDownload(res.data, 'instances.csv');
    });
}

/**
 * Add new instances to a phase defined in the csv/tsv file to be uploaded
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param file file to be uploaded
 * @param get storage hook
 * @returns Promise
 */
export function addInstance(owner: string, project: string, phase: string, file: File, additional: boolean = false, get: Function = () => {}) {
    const token = get('JWT') ?? '';

    const formData = new FormData();
    formData.append('owner', owner);
    formData.append('project', project);
    formData.append('phase', phase);
    formData.append('additional', additional.toString());
    formData.append('file', file);

    return axios.post(`${BACKENDROUTES.INSTANCE}`,
        formData,
        {
            headers: {
                "Authorization": `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(res => res.data);
}


// task specific functions


/**
 * Get the WSSIM Tags for a specific lemma
 * 
 * @param authenticationToken   The authentication token of the requesting user
 * @param owner                 The owner of the project
 * @param project               The name of the project
 * @param phase                 The name of the phase
 * @param lemma                 The lemma in question
 * @return                      WSSIM Tags with same lemma for this phase  
 */
export function useFetchWSSIMTagsOfLemma(owner: string, project: string, phase: string, lemma: string, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<WSSIMTagDto[]>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data);

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.INSTANCE}/wssimtag-lemma?owner=${owner}&project=${project}&phase=${phase}&lemma=${lemma}` : null, queryPhaseDataFetcher);

    return {
        data: data ? data.map(WSSIMTag.fromDto) : [].map(WSSIMTag.fromDto),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}