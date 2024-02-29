// react
import { useState } from "react";

// toast
import { toast } from "react-toastify";
import { MdOutlineTune } from "react-icons/md";

// icons
import { FiFileText, FiKey, FiMonitor, FiServer } from "react-icons/fi";
import { SiOpenai } from "react-icons/si";
import { RiShieldKeyholeFill } from "react-icons/ri";
import { TbPrompt } from "react-icons/tb";
import { VscCalendar, VscSymbolKeyword } from "react-icons/vsc";


// hooks
import useStorage from "../../../lib/hook/useStorage";

// services
import { useFetchComputationAnnotatorsOfPhase } from "../../../lib/service/annotator/AnnotatorResource";

// models
import Phase from "../../../lib/model/phase/model/Phase";
import Annotator from "../../../lib/model/annotator/model/Annotator";
import StartComputationalAnnotationCommand from "../../../lib/model/phase/command/StartComputationalAnnotationCommand";

// components
import DropdownSelect from "../../generic/dropdown/dropdownselect";
import { startComputationalAnnotation } from "../../../lib/service/phase/PhaseResource";
import OpenAIModel from "../../../lib/model/computationalannotator/openaimodel/model/OpenAIModel";
import { chatGptLexsubAnnotation, chatGptUsePairAnnotation, chatLexSubTutorialAnnotation, chatUsePairTutorialAnnotation, chatWSSIMAnnotation, chatWSSIMTutorialAnnotation, useFetchAllOpenAIMode } from "../../../lib/service/computationalAnnotator/ComputationalAnnotatorResource";
import UsePairComputationalAnnotatorCommand from "../../../lib/model/computationalannotator/ComputationalAnnotatorCommand";
import BasicCheckbox from "../../generic/checkbox/basiccheckbox";
import { Router, useRouter } from "next/router";
import ANNOTATIONTYPES from "../../../lib/AnnotationTypes";
import AnnotationType from "../../../lib/model/annotationtype/model/AnnotationType";
import ComputationalAnnotatorCommand from "../../../lib/model/computationalannotator/ComputationalAnnotatorCommand";
import { useFetchPagedUsePairInstance } from "../../../lib/service/instance/InstanceResource";
import IconButtonOnClick from "../../generic/button/iconbuttononclick";


const ComputationalAnnotationModal: React.FC<{
    isOpen: boolean, closeModalCallback: Function, phase: Phase,
    onClickFineTuning: Function,
    mutateCallback: Function, setTutorialHistory: Function,
    openProcessingModal: Function, setLoadingStatus: Function
}> = ({ isOpen, closeModalCallback, phase, onClickFineTuning,
    mutateCallback, setTutorialHistory, openProcessingModal, setLoadingStatus }) => {

        // hooks
        const storage = useStorage();

        const Router = useRouter();
      
        const eligibleAnnotators = useFetchComputationAnnotatorsOfPhase(phase?.getId().getOwner(), phase?.getId().getProject(), phase?.getId().getPhase(), isOpen && !!phase);

        const openaimodels = useFetchAllOpenAIMode();
        openaimodels.openaimodels.sort((a, b) => a.getName().localeCompare(b.getName()));


        // modal
        const [modalState, setModalState] = useState({
            selectedAnnotator: [] as Annotator[]
        });

        const [selectedFile, setSelectedFile] = useState(null as File | null);

        const [showApiKey, setShowApiKey] = useState(false);
        const [tutorial, setTutorial] = useState(false);

        const [chatGptModalState, setChatGptModalState] = useState({
            apiKey: "",
            model: null as unknown as OpenAIModel,
            otherModel: "",
            prompt: "",
            lemma: ""
        });
   
        const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
            const file = event.target.files && event.target.files[0];
            if (file) {
                // Check file extension
                if (!file.name.toLowerCase().endsWith('.md')) {
                    toast.warning('Please select a Markdown (.md) file.');
                    return;
                }
                setSelectedFile(file);

                // Read file contents
                const reader = new FileReader();
                reader.onload = (e) => {
                    const fileContent = e.target?.result as string;
                    // Set state with the file content
                    setChatGptModalState({
                        ...chatGptModalState,
                        prompt: fileContent,
                    });
                };
                reader.readAsText(file);
            }
        };

        const resetChatGptModalState = () => {
            setChatGptModalState({
                ...chatGptModalState,
                apiKey: "",
                model: null as unknown as OpenAIModel,
                prompt: "",
            });

        }

        const onConfirm = () => {

            if (!modalState.selectedAnnotator.some(Annotator => Annotator.getVisiblename() === "ChatGpt")) {


                computationalAnnotation();
            }
            if (modalState.selectedAnnotator.some(Annotator => Annotator.getVisiblename() === "ChatGpt")) {
                chatGptAnnotation()
                return;
            }
            if (
                modalState.selectedAnnotator.some(Annotator => Annotator.getVisiblename() === "ChatGpt") &&
                (modalState.selectedAnnotator.some(Annotator => Annotator.getVisiblename() === "UREL") ||
                    modalState.selectedAnnotator.some(Annotator => Annotator.getVisiblename() === "deep"))
            ) {
                computationalAnnotation();
                chatGptAnnotation();
            }

        }

        const chatGptAnnotation = () => {
            const command = verifyComputationalAnnotation(chatGptModalState, phase)
            if (command == null) {
                return;
            }

            const username = storage.get('USER');

            if (!username) {
                toast.warning("This should not happen. Please contact the administrator.");
                return;
            }

            if (phase?.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_USEPAIR) {
                closeModalCallback();
                setLoadingStatus(true);
                chatGptUsePairAnnotation(command, storage.get)
                    .then((data) => {
                        setLoadingStatus(false);
                        resetChatGptModalState();
                    })
                    .then(() => {
                        Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getId().getPhase()}/judgement`);
                    })
                    .catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error(error.response.data.message + "!");
                            resetChatGptModalState();
                            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getId().getPhase()}/judgement`);
                        } else {
                            toast.warning(error.response.data.message);
                        }
                    });
            }
            if (phase?.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_LEXSUB) {
                closeModalCallback();
                setLoadingStatus(true);
                chatGptLexsubAnnotation(command, storage.get)
                .then(() => {
                    setLoadingStatus(false);
                    resetChatGptModalState();
                })
                .then(() => {
                    Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getId().getPhase()}/judgement`);
                })
                    .catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error(error.response.data.message + "!");
                            resetChatGptModalState();
                            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getId().getPhase()}/judgement`);

                        } else {
                            toast.warning(error.response.data.message);
                        }
                    });
            }
            if (phase?.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_WSSIM) {
                closeModalCallback();
                setLoadingStatus(true);
                chatWSSIMAnnotation(command, storage.get)
                .then(() => {
                    setLoadingStatus(false);
                    resetChatGptModalState();
                })
                .then(() => {
                    Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getId().getPhase()}/judgement`);
                })
                    .catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error(error.response.data.message + "!");
                            resetChatGptModalState();
                            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getId().getPhase()}/judgement`);

                        } else {
                            toast.warning(error.response.data.message);
                        }
                    });
            }
        }

        const computationalAnnotation = () => {
            const command = verifyAndAddCommand(phase, modalState.selectedAnnotator);

            if (command) {
                startComputationalAnnotation(command, storage.get).then(() => {
                    toast.success("Successfully started computational annotation.");
                    setModalState({
                        selectedAnnotator: []
                    });
                    mutateCallback();
                    closeModalCallback();
                }).catch((error) => {
                    if (error?.response?.status === 500) {
                        toast.error("Error while adding user: " + error.response.data.message + "!");
                    } else {
                        toast.warning("The system is currently not available, please try again later!");
                    }
                    setModalState({
                        selectedAnnotator: []
                    });
                });
            }

        }


        const onConfirmTutorial = () => {
            
            const command = verifyComputationalAnnotation(chatGptModalState, phase)
            if (command == null) {
                return;
            }

            const username = storage.get('USER');

            if (!username) {
                toast.warning("This should not happen. Please contact the administrator.");
                return;
            }

            if (phase?.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_USEPAIR) {

                toast.success("Successfully started computational annotation.")
                closeModalCallback();
                setLoadingStatus(true);
                chatUsePairTutorialAnnotation(command, storage.get)
                    .then((data) => { setTutorialHistory(data) })
                    .then(() => { setLoadingStatus(false) })
                    .then(() => { openProcessingModal(true) })
                    .catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error(error.response.data.message + "!");
                            resetChatGptModalState();
                            setLoadingStatus(false);
                        } else {
                            toast.warning(error);
                            setLoadingStatus(false);

                        }
                    });
            }

            if (phase?.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_LEXSUB) {
                toast.success("Successfully started computational annotation.")
                closeModalCallback();
                setLoadingStatus(true);
                chatLexSubTutorialAnnotation(command, storage.get)
                    .then((data) => { setTutorialHistory(data) })
                    .then(() => { setLoadingStatus(false) })
                    .then(() => { openProcessingModal(true) })
                    .catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error(error.response.data.message + "!");
                            resetChatGptModalState();
                            setLoadingStatus(false);

                        } else {
                            toast.warning(error);
                            setLoadingStatus(false);

                        }
                    });
            }

            if (phase?.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_WSSIM) {

                toast.success("Successfully started computational annotation.")
                closeModalCallback();
                setLoadingStatus(true);
                chatWSSIMTutorialAnnotation(command, storage.get)
                    .then((data) => { setTutorialHistory(data) })
                    .then(() => { setLoadingStatus(false) })
                    .then(() => { openProcessingModal(true) })
                    .catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error(error.response.data.message + "!");
                            resetChatGptModalState();
                            setLoadingStatus(false);

                        } else {
                            toast.warning(error);
                            setLoadingStatus(false);

                        }
                    });
            }

        }

        const openFineTune = () => {
            const isOpen = true;
            const key = chatGptModalState.apiKey;
            onClickFineTuning(isOpen, key);

        }

        const onCancel = () => {
            toast.info("Canceled computation.");
            setModalState({
                selectedAnnotator: []
            });
            resetChatGptModalState();
            closeModalCallback();
        }

        if (!isOpen || !phase) {
            return null;
        }

        return (
            <div className="relative z-10 font-dm-mono-medium" onClick={() => onCancel()}>
                <div className="fixed inset-0 bg-base16-gray-500 bg-opacity-75" />

                <div className="fixed z-10 inset-0 overflow-y-auto">
                    <div className="flex items-center justify-center min-h-full">
                        <div className="relative bg-white overflow-hidden shadow-md py-4 px-8  max-w-xl w-full" onClick={(e: any) => e.stopPropagation()}>
                            <div className="mx-4">
                                <div className="flex flex-col items-left mt-6">
                                    <div className="font-black text-xl">
                                        Start Computational Annotation
                                    </div>
                                    <div className="font-dm-mono-regular my-2">
                                        You are about to start a computational annotation for phase {phase.getName()}. Please select the computational annotators you want to use.
                                        Note that the this process might take some time depending on the amount of data and the computational annotators you selected.
                                    </div>

                                    <div className="flex flex-col items-left my-6">
                                        <div className="font-bold text-lg">
                                            Select Computational Annotators
                                        </div>
                                        <div className="flex items-center border-b-2 py-2 px-3 mt-2">
                                            <DropdownSelect
                                                icon={<FiMonitor className="basic-svg" />}
                                                items={eligibleAnnotators.annotators}
                                                selected={modalState.selectedAnnotator}
                                                onSelectFunction={(user: Annotator) => setModalState({
                                                    ...modalState,
                                                    selectedAnnotator: calculateNewSelectedUserArray(user, modalState.selectedAnnotator)
                                                })}
                                                message={
                                                    eligibleAnnotators.annotators.length === 0 ? "No Computational Annotators available" :
                                                        modalState.selectedAnnotator.length > 0 ?
                                                            `Number selected Computational annotators: ${modalState.selectedAnnotator.length}`
                                                            : "No Computational annotators selected"
                                                } />
                                        </div>
                                        {
                                            modalState.selectedAnnotator.some(
                                                (annotator) =>
                                                    annotator.getId().getUser() === "ChatGpt") && (
                                                <div className="flex flex-col items-left my-6">

                                                    <div className="font-bold-mono text-m">OpenAI Key</div>
                                                    <div className="flex items-center border-b-2 py-2 px-3 mt-2">
                                                        <RiShieldKeyholeFill className="basic-svg" />
                                                        <input
                                                            id="apiKey"
                                                            name="apiKey"
                                                            className="pl-3 flex flex-auto outline-none border-none"
                                                            placeholder="Api key"
                                                            type={showApiKey ? "text" : "password"}
                                                            value={chatGptModalState.apiKey}
                                                            onChange={(e) =>
                                                                setChatGptModalState({
                                                                    ...chatGptModalState,
                                                                    apiKey: e.target.value,
                                                                })
                                                            }
                                                        />
                                                    </div>
                                                    <div className="py-2 px-3 text-sm">
                                                        <BasicCheckbox
                                                            selected={showApiKey}
                                                            description={"Show API KEY"}
                                                            onClick={() => setShowApiKey(!showApiKey)}
                                                        />
                                                    </div>

                                                    <div className="flex justify-between items-center">
                                                        <div className="font-bold-mono text-m">Choose Model</div>
                                                       {/* y */}
                                                    </div>
                                                    <div className="flex items-center border-b-2 py-2 px-3 mt-2">
                                                        <DropdownSelect
                                                            icon={<SiOpenai className="basic-svg" />}
                                                            items={openaimodels.openaimodels}
                                                            selected={chatGptModalState.model ? [chatGptModalState.model] : []}
                                                            onSelectFunction={(model) =>
                                                                setChatGptModalState({
                                                                    ...chatGptModalState,
                                                                    model: model,
                                                                })
                                                            }
                                                            message={chatGptModalState.model ? chatGptModalState.model.getVisiblename() : "None selected yet"}
                                                        />
                                                        {chatGptModalState.model &&
                                                            chatGptModalState.model.getVisiblename() === "others" && (
                                                                <div>
                                                                   
                                                                    <input
                                                                        id="model"
                                                                        name="model"
                                                                        className="pl-3 flex flex-auto outline-none border-none"
                                                                        placeholder="Enter model name"
                                                                        type={"text"}
                                                                        value={chatGptModalState.otherModel}
                                                                        onChange={(e) =>
                                                                            setChatGptModalState({
                                                                                ...chatGptModalState,
                                                                                otherModel: e.target.value,
                                                                            })
                                                                        }
                                                                    />
                                                                </div>
                                                            )}

                                                    </div>
                                                    <div className="py-2 px-3 text-sm">
                                                        <BasicCheckbox
                                                            selected={tutorial}
                                                            description={"Test Model With Tutorial"}
                                                            onClick={() => setTutorial(!tutorial)}
                                                        />
                                                    </div>

                                                    <div className="font-bold-mono text-m">Upload {tutorial && (<span>Tutorial</span>)} Prompt</div>
                                                    <div className="py-2 px-3 flex items-center border-b-2 mt-2">
                                                        <TbPrompt className="basic-svg" />
                                                        <input type="file" className="hide-upload-button pl-3 flex flex-auto outline-none border-none text-sm" onChange={handleFileChange} />
                                                    </div>
                                                </div>
                                            )
                                        }


                                    </div>

                                </div>
                            </div>
                            <div className="flex flex-row divide-x-8">
                                <button type="button" className="active:transform active:scale-95 block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 "
                                    onClick={() => onCancel()}>Cancel</button>
                                {!tutorial && (
                                    <button
                                        type="button"
                                        className="active:transform active:scale-95 block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100"
                                        onClick={onConfirm}
                                    >
                                        Confirm
                                    </button>
                                )}
                                {tutorial && (
                                    <button
                                        type="button"
                                        className="active:transform active:scale-95 block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100"
                                        onClick={onConfirmTutorial}
                                    >
                                        Start Tutorial
                                    </button>
                                )}

                            </div>
                        </div>
                    </div>
                </div>

            </div>


        )
    }

export default ComputationalAnnotationModal;

function calculateNewSelectedUserArray(user: Annotator, selectedUser: Array<Annotator>) {
    const filtered = selectedUser.filter(l => l.getName() !== user.getName());

    if (filtered.length === selectedUser.length) {
        return [...selectedUser, user];
    }
    return filtered;
}

function verifyAndAddCommand(phase: Phase, selectedUsers: Annotator[]): StartComputationalAnnotationCommand | null {
    if (selectedUsers === null || selectedUsers.length === 0) {
        toast.warning("Please select at least one computational annotator.");
        return null;
    }
    return new StartComputationalAnnotationCommand(
        phase.getId().getOwner(),
        phase.getId().getProject(),
        phase.getId().getPhase(),
        selectedUsers.map(u => u.getName())
    );
}

function verifyComputationalAnnotation(chatGptModalState: {
    apiKey: string,
    model: OpenAIModel,
    otherModel: string,
    prompt: string,
}, phase: Phase): ComputationalAnnotatorCommand | null {
    if (!chatGptModalState.apiKey) {
        toast.error("Enter api key.");
        return null;
    }

    if ((chatGptModalState.apiKey as string).includes(" ")) {
        toast.error("Api key should not contain any spaces.");
        return null;
    }

    if (!chatGptModalState.model) {
        toast.error("Please choose model.");
        return null;
    }

    if (!chatGptModalState.prompt) {
        toast.error("Prompt must not be blank.");
        return null;
    }

    return new ComputationalAnnotatorCommand(
        phase?.getId().getOwner(),
        phase?.getId().getProject(),
        phase?.getId().getPhase(),
        chatGptModalState.apiKey,
        chatGptModalState.model.getName() === "others" ? chatGptModalState.otherModel : chatGptModalState.model.getVisiblename(),
        chatGptModalState.prompt,
    );
}