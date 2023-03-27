import axios from "axios";
import useSWR from "swr";

// Custom Hooks
import useStorage from "../../hook/useStorage";

// Routes
import BACKENDROUTES from "../../BackendRoutes";

// Models
import PhaseDto from "../../model/phase/dto/PhaseDto";
import Phase from "../../model/phase/model/Phase";

// Commands
import CreatePhaseCommand from "../../model/phase/command/CreatePhaseCommand";
import AddRequirementsCommand from "../../model/phase/command/AddRequirementsCommand";
import StartComputationalAnnotationCommand from "../../model/phase/command/StartComputationalAnnotationCommand";
import { toast } from "react-toastify";

// Custom hooks for fetching phases

/**
 * Returns all phases based on the given query belonging to the given project
 * 
 * @param owner username of the owner of the project
 * @param project project name
 * @param annotationType annotation type of the phases
 * @param tutorial if tutorial phases should be fetched
 * @param fetch if data should be fetched
 * @returns list of all phases
 */
export function useFetchPhases(owner: string, project: string, annotationType: string = '', tutorial: boolean = false, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const queryPhaseFetcher = (url: string) => axios.get<PhaseDto[]>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.PHASE}/by-project?owner=${owner}&project=${project}&annotation-type=${annotationType}&tutorial=${tutorial}` : null, queryPhaseFetcher)

    return {
        phases: data ? data.map(Phase.fromDto) : [] as Phase[],
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }

}

/**
 * Fetches the phase with the given id
 * 
 * @param owner username of the owner of the project
 * @param project project name
 * @param phase phase name
 * @param fetch if data should be fetched
 * @returns phase
 */
export function useFetchPhase(owner: string, project: string, phase: string, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const phaseFetcher = (url: string) => axios.get<PhaseDto>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.PHASE}/phase?owner=${owner}&project=${project}&phase=${phase}` : null, phaseFetcher)

    return {
        phase: data ? Phase.fromDto(data) : null,
        isLoading: !error && !data,
        isError: error,
        mutate: mutate
    }

}

/** 
 * Checks if requesting user has access to annotate phase.
 * 
 * @param owner username of the owner of the project
 * @param project project name
 * @param phase phase name
 * @param fetch if data should be fetched
 * @returns true if user has access to annotate phase  
 */
export function useFetchAnnotationAccess(owner: string, project: string, phase: string, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const accessFetcher = (url: string) => axios.get<boolean>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data).catch(error => {
        toast.info("The phase is not ready for annotation yet. " + error.response.data.message);
        throw new Error(error);
    })

    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.PHASE}/has-access?owner=${owner}&project=${project}&phase=${phase}` : null, accessFetcher)

    return {
        hasAccess: data ? data : undefined,
        isLoading: !error,
        isError: !!error,
        mutate: mutate
    }
}

// Custom functions for posting data to the backend

/**
 * Creates a new phase
 * 
 * @param command command to create a phase
 * @param get function to get data from local storage
 * @returns Promise
 */
export function createPhase(command: CreatePhaseCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.PHASE}/create`,
        command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

/**
 * Close a phase
 * 
 * @param owner username of the owner of the project
 * @param project project name
 * @param phase phase name
 * @param get function to get data from local storage
 * @returns Promise
 */
export function closePhase(owner: string, project: string, phase: string, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.PHASE}/close?owner=${owner}&project=${project}&phase=${phase}`,
        {},
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

/**
 * Add requirements to a phase
 * 
 * @param command command to add requirements to a phase
 * @param get function to get data from local storage
 * @returns Promise
 */
export function addRequirementsToPhase(command: AddRequirementsCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.PHASE}/add-requirements`,
        command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}

export function startComputationalAnnotation(command: StartComputationalAnnotationCommand, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    return axios.post(`${BACKENDROUTES.PHASE}/start-computational-annotation`,
        command,
        {
            headers: { "Authorization": `Bearer ${token}` },
        }
    ).then(res => res.data);
}
