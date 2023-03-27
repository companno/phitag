// react
import { useState } from "react";

// toast
import { toast } from "react-toastify";

// icons
import { FiMonitor } from "react-icons/fi";

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


const ComputationalAnnotationModal: React.FC<{ isOpen: boolean, closeModalCallback: Function, phase: Phase, mutateCallback: Function }> = ({ isOpen, closeModalCallback, phase, mutateCallback }) => {

    // hooks
    const storage = useStorage();

    const eligibleAnnotators = useFetchComputationAnnotatorsOfPhase(phase?.getId().getOwner(), phase?.getId().getProject(), phase?.getId().getPhase(), isOpen && !!phase);

    // modal
    const [modalState, setModalState] = useState({
        selectedAnnotator: [] as Annotator[]
    });

    const onConfirm = () => {
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

    const onCancel = () => {
        toast.info("Canceled computation.");
        setModalState({
            selectedAnnotator: []
        });
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
                                </div>

                            </div>
                        </div>
                        <div className="flex flex-row divide-x-8">
                            <button type="button" className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 " onClick={() => onCancel()}>Cancel</button>
                            <button type="button" className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 " onClick={onConfirm}>Confirm</button>
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