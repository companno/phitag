import axios from "axios";
import useSWR from "swr";
import fileDownload from "js-file-download";

// Custom Hooks
import useStorage from "../../hook/useStorage";

// Routes
import BACKENDROUTES from "../../BackendRoutes";

// Interfaces
import IJudgementDto from "../../model/judgement/dto/IJudgementDto";
import IAddJudgementCommand from "../../model/judgement/command/IAddJudgementCommand";
import { IJudgement, IJudgementConstructor } from "../../model/judgement/model/IJudgement";
import IEditJudgementCommand from "../../model/judgement/command/IEditJudgementCommand";
import IDeleteJudgementCommand from "../../model/judgement/command/IDeleteJudgementCommand";
import PagedGenericDto from "../../model/interfaces/PagedGenericDto";
import UsePairJudgement from "../../model/judgement/usepairjudgement/model/UsePairJudgement";
import PagedUsePairJudgement from "../../model/judgement/usepairjudgement/model/PagedUsePairJudgement";
import WSSIMJudgementDto from "../../model/judgement/wssimjudgement/dto/WSSIMJudgementDto";
import PagedWSSIMJudgement from "../../model/judgement/wssimjudgement/model/PagedWSSIMJudgement";
import UsePairJudgementDto from "../../model/judgement/usepairjudgement/dto/UsePairJudgementDto";

/** 
 * Fetches all judgements of a phase
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param constructor data class to be converted to (implements fromDto, i.e. the convertion function)
 * @param fetch if data should be fetched
 * @returns list of all judgements
 */
export function useFetchJudgements<G extends IJudgement, T extends IJudgementConstructor>(owner: string, project: string, phase: string, constructor: T, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<IJudgementDto[]>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.JUDGEMENT}?owner=${owner}&project=${project}&phase=${phase}` : null, queryPhaseDataFetcher)

    return {
        data: data ? data.map(constructor.fromDto) : [] as G[],
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/** 
 * Fetches all use pair judgements of a phase paged
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param page page number
 * @param fetch if data should be fetched
 * @returns list of all judgements
 */
export function useFetchPagedUsePairJudgements(owner: string, project: string, phase: string, page: number, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<UsePairJudgementDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data);

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.JUDGEMENT}/paged?owner=${owner}&project=${project}&phase=${phase}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedUsePairJudgement.fromDto(data) : PagedUsePairJudgement.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/** 
 * Fetches all wssim judgements of a phase paged
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param page page number
 * @param fetch if data should be fetched
 * @returns list of all judgements
 */
export function useFetchPagedWSSIMJudgements(owner: string, project: string, phase: string, page: number, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<WSSIMJudgementDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data);

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.JUDGEMENT}/paged?owner=${owner}&project=${project}&phase=${phase}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedWSSIMJudgement.fromDto(data) : PagedWSSIMJudgement.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/**
 * Fetches the judgement history of the user of a phase
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param constructor data class to be converted to (implements fromDto, i.e. the convertion function)
 * @param fetch if data should be fetched
 * @returns 
 */
export function useFetchHistory<G extends IJudgement, T extends IJudgementConstructor>(owner: string, project: string, phase: string, constructor: T, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<IJudgementDto[]>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.JUDGEMENT}/history/personal?owner=${owner}&project=${project}&phase=${phase}` : null, queryPhaseDataFetcher)

    return {
        data: data ? data.map(constructor.fromDto) : [] as G[],
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }

}


/** 
 * Fetches all use pair judgements of a user paged
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param page page number
 * @param fetch if data should be fetched
 * @returns list of all judgements
 */
export function useFetchPagedHistoryUsePairJudgements(owner: string, project: string, phase: string, page: number, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<UsePairJudgementDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data);

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.JUDGEMENT}/history/personal/paged?owner=${owner}&project=${project}&phase=${phase}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedUsePairJudgement.fromDto(data) : PagedUsePairJudgement.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}

/** 
 * Fetches all wssim judgements of a user paged
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param page page number
 * @param fetch if data should be fetched
 * @returns list of all judgements
 */
export function useFetchPagedHistoryWSSIMJudgements(owner: string, project: string, phase: string, page: number, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseDataFetcher = (url: string) => axios.get<PagedGenericDto<WSSIMJudgementDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data);

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.JUDGEMENT}/history/personal/paged?owner=${owner}&project=${project}&phase=${phase}&page=${page}` : null, queryPhaseDataFetcher)

    return {
        data: data ? PagedWSSIMJudgement.fromDto(data) : PagedWSSIMJudgement.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }
}


/**
 * Fetches all judgements of a phase as a csv file
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param get storage hook
 * @returns a csv file of the judgements of the phase
 */
export function exportJudgement(owner: string, project: string, phase: string, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.get(`${BACKENDROUTES.JUDGEMENT}/export?owner=${owner}&project=${project}&phase=${phase}`, {
        responseType: 'blob',
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => {
        fileDownload(res.data, 'results.csv');
    });
}

/**
 * Adds judgements to the phase as a csv file 
 * 
 * @param owner owner of the project
 * @param project project name
 * @param phase phase name in the project
 * @param file file to be uploaded
 * @param get storage hook
 * @returns Promise
 */
export function addJudgement(owner: string, project: string, phase: string, file: File, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    const formData = new FormData();
    formData.append('owner', owner);
    formData.append('project', project);
    formData.append('phase', phase);
    formData.append('file', file);

    return axios.post(`${BACKENDROUTES.JUDGEMENT}`,
        formData,
        {
            headers: {
                "Authorization": `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(res => res.data);
}

/**
 * Edit use pair judgement
 * 
 * @param command command containing the judgement
 * @param get storage hook
 */
export function editUsepair(command: IEditJudgementCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/edit/usepair`, command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

/**
 * Edit WSSIM judgement
 * 
 * @param command command containing the judgement
 * @param get storage hook
 * 
 * @returns Promise
 */
export function editWssim(command: IEditJudgementCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/edit/wssim`, command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

/**
 * Delete Use Pair judgement
 * 
 * @param command command containing the judgement
 * @param get storage hook
 */
export function deleteUsepair(command: IDeleteJudgementCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/delete/usepair`, command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

/**
 * Delete WSSIM judgement
 * 
 * @param command command containing the judgement
 * @param get storage hook
 */
export function deleteWssim(command: IDeleteJudgementCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/delete/wssim`, command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}



/** 
 * Add a judgement to the phase (i.e. annotate an instance of a phase)
 * 
 * @param command command containing the judgement
 * @returns Promise
 */
export function annotateUsepair(command: IAddJudgementCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/annotate/usepair`, command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);

}

/**
 * Add a bulk of judgements to the phase (i.e. annotate instances of a phase)
 */
export function bulkAnnotateUsepair(commands: IAddJudgementCommand[], get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/annotate/usepair/bulk`, commands,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

/**
 * Add a judgement to the phase where Task is WSSIM
 * 
 * @param command command containing the judgement
 * @returns Promise
 */
export function annotateWSSIM(command: IAddJudgementCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/annotate/wssim`, command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

/**
 * Add a bulk of judgements to phase of task type WSSIM
 */
export function bulkAnnotateWSSIM(commands: IAddJudgementCommand[], get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.JUDGEMENT}/annotate/wssim/bulk`, commands,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}


