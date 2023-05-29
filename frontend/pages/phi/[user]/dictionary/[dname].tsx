import Router, { useRouter } from "next/router";
import useAuthenticated from "../../../../lib/hook/useAuthenticated";
import { useEffect, useState } from "react";
import { createDictionaryEntry, deleteDictionaryEntry, useFetchDictionaryEntries } from "../../../../lib/service/dictionary/DictionaryEntryResource";
import { toast } from "react-toastify";
import Layout from "../../../../components/generic/layout/layout";
import Head from "next/head";
import { FiCheck, FiEdit2, FiPlus, FiSearch, FiTrash, FiX } from "react-icons/fi";
import useStorage from "../../../../lib/hook/useStorage";
import HelpButton from "../../../../components/generic/button/helpbutton";
import DictionaryEntry from "../../../../lib/model/dictionary/entry/model/DictionaryEntry";
import { updateDictionaryEntry } from "../../../../lib/service/dictionary/DictionaryEntryResource";

const DictionaryPage = () => {
    // Data & Hooks
    const authenticated = useAuthenticated();
    const router = useRouter();
    const { user: uname, dname } = router.query as { user: string, dname: string };

    const [searchField, setSearchField] = useState({
        headword: "",
        addModal: false,
    });

    const dictionaries = useFetchDictionaryEntries(dname, uname, searchField.headword, "", 0, router.isReady);

    useEffect(() => {
        if (authenticated.isReady && !authenticated.isAuthenticated) {
            toast.info("Session expired, please login again.");
            Router.push("/");
        }
    }, [authenticated]);


    return (
        <Layout>

            <Head>
                <title>PhiTag: Dictionary</title>
            </Head>

            <div className="flex flex-col m-auto w-full xl:w-4/5 xl:py-16 justify-center items-center">

                <div className="flex flex-row w-full justify-center items-center mb-4">
                    <div className="flex font-dm-mono-medium font-bold text-2xl pr-4">
                        {dname}
                    </div>

                    <div className="flex flex-row w-full ">

                        <div className="flex flex-row w-full basis-full items-center border-b-2 py-2 px-3 my-4">
                            <input
                                className="pr-3 flex flex-auto outline-none border-none font-dm-sans-medium font-bold"
                                placeholder="Search headword"
                                type={"text"}
                                value={searchField.headword}
                                onChange={(e) => setSearchField({
                                    ...searchField,
                                    headword: e.target.value
                                })} />
                            <FiSearch className='basic-svg' />
                        </div>

                        <div className="flex items-center my-4 ml-4">
                            <HelpButton
                                title="Help: Dictionary"
                                tooltip="Help: Dictionary"
                                text="This is the dictionary page. Here you can view and search all the entries in the dictionary. You can also create new entries by clicking the plus button in the bottom right corner."
                                reference="/guides/help-dictionary"
                            />
                        </div>
                    </div>
                </div>

                <DictionaryView entries={dictionaries.data?.content || []} mutateCallback={() => dictionaries.mutate()} />
            </div>



            <div className="flex w-16 h-16 justify-center items-center rounded-full absolute bottom-10 right-10 shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200"
                onClick={() => setSearchField({ ...searchField, addModal: true })}>
                <FiPlus className="h-8 w-8" />
            </div>

            {searchField.addModal && (
                <CreateDictionaryEntryModal closeCallback={() => setSearchField({ ...searchField, addModal: false })} mutateCallback={() => dictionaries.mutate()} />
            )}

        </Layout>
    );
}

export default DictionaryPage;

const DictionaryView = ({ entries, mutateCallback }: { entries: DictionaryEntry[], mutateCallback: () => void }) => {

    const [selectedEntry, setSelectedEntry] = useState<string>("");

    return (
        <div className="flex flex-row w-full p-8">
            {/* Entries list */}
            <div className="w-1/5">
                <DictionaryEntriesListView entries={entries} onClick={(entry) => setSelectedEntry(entry)} selectedEntry={selectedEntry} />
            </div>

            {/* splitter */}
            <div className="border-r-2 mx-8"></div>

            {/* Full entry view */}
            <div className="flex flex-col w-full">
                {/* {selectedEntry && ( */}
                <DictionaryEntryFullView entry={entries.find((entry) => entry.id.id === selectedEntry) ?? null} mutateCallback={mutateCallback} />
                {/* )} */}
            </div>

        </div>
    )
}

const DictionaryEntriesListView = ({ entries, selectedEntry, onClick }: { entries: DictionaryEntry[], selectedEntry: string, onClick: (entry: any) => void }) => {

    // This presents the entries in a list view with headword and it is possible to click on the entry to view it in full.

    return (
        <div className="flex flex-col">
            {entries.map((entry) => (
                <div key={entry.id.id} onClick={() => onClick(entry.id.id)}
                    className={`flex border-b-2 border-white hover:border-base16-green transition-all duration-700 cursor-pointer py-2 px-3 my-4` + (entry.id.id === selectedEntry ? " border-base16-green" : "")}>
                    <div className="flex flex-auto font-dm-mono-medium font-bold text-xl">
                        {entry.headword}
                        <span className="font-dm-mono-regular font-bold text-base ml-4 text-base16-gray-500 self-center">
                            {entry.partofspeech}
                        </span>
                    </div>
                </div>
            ))}
        </div>
    )


}

const DictionaryEntryFullView = ({ entry, mutateCallback }: { entry: DictionaryEntry | null, mutateCallback: () => void }) => {

    const onDelete = async ({ entry }: { entry: DictionaryEntry }) => {
        deleteDictionaryEntry(entry.id.id, entry.id.dname, entry.id.uname, useStorage().get)
            .then(() => {
                toast.success("Entry deleted successfully.");
                mutateCallback();
            })
            .catch((err) => {
                toast.error("Failed to delete entry.");
                console.error(err);
            })
    }


    // This presents the entry in full with all the information.
    if (!entry) {
        return (
            <div className="flex flex-col h-full">
                <div className="flex m-auto justify-center items-center font-dm-mono-medium font-bold text-2xl">
                    Select an entry to view it in full.
                </div>
            </div>
        )
    }

    return (
        <div key={entry.id.id} className="flex flex-col h-full">
            <DictionaryEntryHeaderView entry={entry} mutateCallback={mutateCallback} />

            <div className="mt-auto self-end flex w-12 h-12 justify-center items-center rounded-full shadow-md cursor-pointer hover:bg-base16-red transition-all duration-500"
                onClick={() => onDelete({ entry })}>
                <FiTrash className="h-6 w-6" />
            </div>
        </div>
    )
}

const DictionaryEntryHeaderView = ({ entry, mutateCallback }: { entry: DictionaryEntry, mutateCallback: () => void }) => {

    const [edit, setEdit] = useState<boolean>(false);
    const [newentry, setNewentry] = useState({
        headword: "",
        partofspeech: "",
    });

    const onSubmit = async () => {
        updateDictionaryEntry(entry.id.id, entry.id.dname, entry.id.uname, newentry.headword || entry.headword, newentry.partofspeech || entry.partofspeech, useStorage().get)
            .then(() => {
                setEdit(false);
                mutateCallback();
                toast.success("Entry updated successfully!");
            })
            .catch((err) => {
                toast.error("Failed to update entry!");
            })
    }

    if (edit) {
        return (
            <div className="flex flex-row items-center justify-between">
                <div className="flex flex-col items-start space-y-4">
                    <div className="flex flex-row items-end">
                        <span className="font-dm-mono-medium font-bold text-2xl pr-3">
                            Headword:
                        </span>
                        <input
                            className="outline-none border-b-2 font-dm-mono-medium font-black text-4xl"
                            placeholder="Headword"
                            type={"text"}
                            value={newentry.headword || entry.headword}
                            onChange={(e) => setNewentry({
                                ...entry,
                                headword: e.target.value
                            })} />
                    </div>
                    <div className="flex flex-row">
                        <span className="font-dm-mono-medium font-bold text-2xl pr-3">
                            Part of Speech:
                        </span>
                        <input
                            className="outline-none border-b-2 font-dm-mono-regular font-bold text-2xl text-base16-gray-500"
                            placeholder="Part of speech"
                            type={"text"}
                            value={newentry.partofspeech || entry.partofspeech}
                            onChange={(e) => setNewentry({
                                ...entry,
                                partofspeech: e.target.value
                            })} />
                    </div>
                </div>

                <div className="flex flex-row space-x-4">
                    <div className="flex w-12 h-12 justify-center items-center rounded-full shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200"
                        onClick={onSubmit}>
                        <FiCheck className="h-6 w-6" />
                    </div>
                    <div className="flex w-12 h-12 justify-center items-center rounded-full shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200"
                        onClick={() => setEdit(false)}>
                        <FiX className="h-6 w-6" />
                    </div>
                </div>

            </div>
        )

    }

    return (
        <div className="flex flex-row items-center justify-between">
            <div className="flex items-end">
                <div className="font-dm-mono-medium font-black text-4xl">
                    {entry.headword}
                </div>
                <span className="font-dm-mono-regular font-bold text-2xl ml-4 text-base16-gray-500">
                    {entry.partofspeech}
                </span>
            </div>

            <div className="flex w-12 h-12 justify-center items-center rounded-full shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200"
                onClick={() => setEdit(true)}>
                <FiEdit2 className="h-6 w-6" />
            </div>

        </div>
    )
}

const CreateDictionaryEntryModal = ({ closeCallback, mutateCallback }: { closeCallback: () => void, mutateCallback: () => void }) => {

    const router = useRouter();
    const { user: uname, dname } = router.query as { user: string, dname: string };

    const [entry, setEntry] = useState({
        headword: "",
        pos: "",
    });

    const onSubmit = () => {
        createDictionaryEntry(dname, uname, entry.headword, entry.pos, useStorage().get)
            .then(() => {
                toast.success("Entry created!");
                mutateCallback();
                cleanUp();
            }).catch((err) => {
                toast.error("Failed to create dictionary!");
            });
    }

    const onCancel = () => {
        toast.info("Cancelled creating dictionary entry.");
        cleanUp();
    }

    const cleanUp = () => {
        setEntry({
            headword: "",
            pos: "",
        });
        closeCallback();
    };

    return (
        <div className="relative z-10 font-dm-mono-medium" onClick={() => onCancel()}>
            <div className="fixed inset-0 bg-base16-gray-500 bg-opacity-75" />

            <div className="fixed z-10 inset-0 overflow-y-auto">
                <div className="flex items-center justify-center min-h-full">
                    <div className="relative bg-white overflow-hidden shadow-md py-4 px-8  max-w-xl w-full" onClick={(e: any) => e.stopPropagation()}>
                        <div className="mx-4">
                            <div className="flex flex-col items-left mt-6">
                                <div className="font-black text-xl">
                                    Create Dictionary Entry
                                </div>
                                <div className="font-dm-mono-regular my-2">
                                    Create a new dictionary entry.
                                </div>

                                <div className="flex flex-col items-left my-6">
                                    <div className="font-bold text-lg">
                                        Headword
                                    </div>
                                    <div className="flex items-center border-b-2 py-2 px-3 mt-2">
                                        <FiEdit2 className="basic-svg" />
                                        <input
                                            id="headword"
                                            name="headword"
                                            className="pl-3 flex flex-auto outline-none border-none"
                                            type={"text"}
                                            placeholder="Headword"
                                            value={entry.headword}
                                            onChange={(e: any) => {
                                                setEntry({ ...entry, headword: e.target.value });
                                            }}
                                        />
                                    </div>
                                </div>

                                <div className="flex flex-col items-left my-6">
                                    <div className="font-bold text-lg">
                                        Part of Speech
                                    </div>
                                    <div className="flex items-center border-b-2 py-2 px-3 mt-2">
                                        <FiEdit2 className="basic-svg" />
                                        <input
                                            id="pos"
                                            name="Part of Speech"
                                            className="pl-3 flex flex-auto outline-none border-none"
                                            type={"text"}
                                            placeholder="Part of Speech"
                                            value={entry.pos}
                                            onChange={(e: any) => {
                                                setEntry({ ...entry, pos: e.target.value });
                                            }}
                                        />
                                    </div>
                                </div>

                            </div>
                        </div>
                        <div className="flex flex-row divide-x-8">
                            <button type="button" className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 " onClick={() => onCancel()}>Cancel</button>
                            <button type="button" className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 " onClick={onSubmit}>Confirm</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}