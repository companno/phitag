import axios from "axios";
import PagedDictionary from "../../model/dictionary/dictionary/model/PagedDictionary";
import BACKENDROUTES from "../../BackendRoutes";
import useStorage from "../../hook/useStorage";
import useSWR from "swr";
import PagedGenericDto from "../../model/interfaces/PagedGenericDto";
import DictionaryDto from "../../model/dictionary/dictionary/dto/DictionaryDto";

export function useFetchDictionaries(uname: string, page: number, fetch: boolean = true) {
    const { get } = useStorage();
    const token = get('JWT') ?? '';

    const dictionaryDataFetcher = (url: string) => axios.get<PagedGenericDto<DictionaryDto>>(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(res => res.data)


    const { data, error, mutate } = useSWR(fetch ? `${BACKENDROUTES.DICTIONARY}?uname=${uname}&page=${page}` : null, dictionaryDataFetcher)

    return {
        data: data ? PagedDictionary.fromDto(data) : PagedDictionary.empty(),
        isLoading: !error && !data,
        isError: error,
        mutate
    }
}

export function createDictionary(uname: string, dname: string, description: string, file: File | null, get: Function = () => { }) {
    const token = get('JWT') ?? '';

    const formData = new FormData();
    formData.append('uname', uname);
    formData.append('dname', dname);
    formData.append('description', description);
    if (file) {
        formData.append('file', file);
    }

    return axios.post(`${BACKENDROUTES.DICTIONARY}/create`, formData,
        {
            headers: {
                "Authorization": `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(res => res.data);
}