// React
import { useEffect, useRef, useState } from "react";

// Next Components

// React Icons
import { FiArrowLeft, FiArrowRight, FiEdit2, FiLayers } from "react-icons/fi";

// services
import { useFetchAllAnnotationTypes } from "../../../lib/service/annotationtype/AnnotationTypeResource";
import { useFetchSelfEntitlement } from "../../../lib/service/annotator/AnnotatorResource";
import { useFetchPhases } from "../../../lib/service/phase/PhaseResource";

// Component
import PhaseCard from "../../generic/card/phasecard";
import Checkbox from "../../generic/checkbox/checkbox";
import DropdownSelect from "../../generic/dropdown/dropdownselect";
import AddRequirementsModal from "../modal/addrequirementsmodal";
import HelpButton from "../../generic/button/helpbutton";
import IconButtonOnClick from "../../generic/button/iconbuttononclick";
import CreatePhaseModal from "../modal/createphasemodal";
import LoadingComponent from "../../generic/loadingcomponent";
import ComputationalAnnotationModal from "../modal/computationalannotationmodal";

// models
import AnnotationType from "../../../lib/model/annotationtype/model/AnnotationType";
import Phase from "../../../lib/model/phase/model/Phase";
import Project from "../../../lib/model/project/model/Project";
import ENTITLEMENTS from "../../../lib/model/entitlement/Entitlements";



const PhaseCarousel: React.FC<{ project: Project }> = ({ project }) => {

    // search phase annotation type and tutorial
    const [searchFilter, setSearchFilter] = useState({
        annotationType: null as unknown as AnnotationType | null,
        tutorial: false,
    });


    // data
    const phases = useFetchPhases(project?.getId().getOwner(), project?.getId().getName(), searchFilter.annotationType?.getName(), searchFilter.tutorial, !!project);
    const entitlement = useFetchSelfEntitlement(project?.getId().getOwner(), project?.getId().getName(), !!project);
    const annotationTypes = useFetchAllAnnotationTypes()


    // modal
    const [modalState, setModalState] = useState({
        isOpenAddDataModal: false,
        isOpenAddRequirementsModal: false,
        isOpenComputationModal: false,
        phase: null as unknown as Phase,
    });

    const [createPhaseModalState, setCreatePhaseModalState] = useState({
        createTutorialModal: false,
        createPhaseModal: false,
    });

    // div width for carousel
    const [startCard, setStartCard] = useState(0);
    const refCarousel = useRef<HTMLDivElement>(null);
    const maxElements = useWindowSize(refCarousel);

    if (!project || phases.isLoading || phases.isError === null || annotationTypes.isLoading || annotationTypes.annotationTypes === null) {
        return (
            <div ref={refCarousel}>
                <LoadingComponent />;
            </div>
        );
    }

    return (
        <div ref={refCarousel} className="w-full flex flex-col">

            <div className="w-full mt-8 flex flex-row items-center">
                <div className="mx-4 flex ">
                    <Checkbox
                        selected={searchFilter.tutorial}
                        description={"Is Tutorial"}
                        onClick={() => setSearchFilter({
                            ...searchFilter,
                            tutorial: !searchFilter.tutorial
                        })} />
                </div>
                <div className="w-fit grow flex md:px-4">
                    <DropdownSelect
                        icon={<FiEdit2 className="basic-svg" />}
                        items={annotationTypes.annotationTypes}
                        selected={searchFilter.annotationType ? [searchFilter.annotationType] : []}
                        onSelectFunction={(annotationType) => { setSearchFilter({ ...searchFilter, annotationType: calculateNewAnnotationType(annotationType, searchFilter.annotationType) }) }}
                        message={searchFilter.annotationType ? searchFilter.annotationType.getVisiblename() : "None selected"} />
                </div>
                <div className="mx-4 flex flex-row space-x-4">
                    <IconButtonOnClick
                        icon={<FiLayers className="basic-svg" />}
                        onClick={() => { setCreatePhaseModalState({ ...createPhaseModalState, createPhaseModal: true }) }}
                        tooltip="Add Phase"
                        hide={!(project?.isActive() && entitlement.entitlement === ENTITLEMENTS.ADMIN)} />

                    <HelpButton
                        title="What are Phases"
                        tooltip="Help: Phases"
                        text="Phases in a project are used to divide the project into different parts. 
                        Each phase has its own annotation types, sampling strategy and annotation requirements.
                        You can add a phase by clicking on the plus button.
                        You can also add tutorial phases, which are used to teach annotators how to annotate a specific annotation type and validate the annotators ability to annotate."
                        reference="/guide/how-to-annotation-project"

                    />
                </div>
            </div>

            <div className="border-b-2 mt-2 mb-6" />


            <div className="w-full flex flex-row space-x-12 justify-center">
                {
                    phases.phases.slice(startCard, startCard + maxElements).map((phase) => {
                        return <PhaseCard
                            key={phase.getId().getPhase()}
                            phase={phase}
                            entitlement={entitlement.entitlement}
                            refreshCallback={() => phases.mutate()}

                            onClickRequirements={() => setModalState({
                                ...modalState,
                                isOpenAddRequirementsModal: true,
                                phase: phase,
                            })}

                            onClickComputation={() => setModalState({
                                ...modalState,
                                isOpenComputationModal: true,
                                phase: phase,
                            })}
                        />
                    })
                }
                {
                    getDummyCards(phases.phases.length - startCard, maxElements).map((_, index) => {
                        return <div key={index} className="w-80 h-auto" />
                    })
                }
            </div>

            <div className="flex flex-row justify-between mt-8">
                <div className="flex items-center" onClick={() => {
                    if (startCard === 0) {
                        setStartCard(phases.phases.length - 1);
                    } else {
                        setStartCard(startCard - 1);
                    }
                }}>
                    <div className="shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200">
                        <div className="m-6">
                            <FiArrowLeft className="h-8 w-8" />
                        </div>
                    </div>
                </div>

                <div className="flex items-center" onClick={() => {
                    if (startCard === phases.phases.length - 1) {
                        setStartCard(0);
                    } else {
                        setStartCard(startCard + 1);
                    }
                }}>
                    <div className="shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200">
                        <div className="m-6">
                            <FiArrowRight className="h-8 w-8" />
                        </div>
                    </div>
                </div>
            </div>

            <CreatePhaseModal isOpen={createPhaseModalState.createPhaseModal} closeModalCallback={() => {
                setCreatePhaseModalState({ ...createPhaseModalState, createPhaseModal: false })
            }} project={project} mutateCallback={phases.mutate} />


            <AddRequirementsModal isOpen={modalState.isOpenAddRequirementsModal} closeModalCallback={() => {
                setModalState({
                    ...modalState,
                    isOpenAddRequirementsModal: false,
                    phase: null as unknown as Phase,
                });
            }} phase={modalState.phase} mutateCallback={phases.mutate} />


            <ComputationalAnnotationModal isOpen={modalState.isOpenComputationModal} closeModalCallback={
                () => {
                    setModalState({
                        ...modalState,
                        isOpenComputationModal: false,
                        phase: null as unknown as Phase,
                    });
                }
            } phase={modalState.phase} mutateCallback={() => { }} />
        </div>

    );
}

export default PhaseCarousel;

function getDummyCards(numberCardsDisplayed: number, maxElements: number) {
    const cards = [];
    for (let i = 0; i < maxElements - numberCardsDisplayed; i++) {
        cards.push('dummy');
    }
    return cards;
}

function calculateNewAnnotationType(newAnnotationType: AnnotationType, annotationTypesSelected: AnnotationType | null): AnnotationType | null {
    if (annotationTypesSelected === null) {
        return newAnnotationType;
    } else if (annotationTypesSelected.getName() === newAnnotationType.getName()) {
        return null;
    }

    return newAnnotationType;
}

// Hook, thanks to https://stackoverflow.com/questions/63406435/how-to-detect-window-size-in-next-js-ssr-using-react-hook
function useWindowSize(refCarousel: any) {

    const calculateMaxCards = (refCarousel: any) => {
        let max = 1;
        if (refCarousel.current) {
            const width = refCarousel.current.offsetWidth;
            max = Math.floor(width / 240);
            max = max > 4 ? 4 : max;
            max = max < 1 ? 1 : max;
        }

        return max;
    }

    const [maxElements, setMaxElements] = useState(0);

    useEffect(() => {
        function handleResize() {
            setMaxElements(calculateMaxCards(refCarousel));
        }

        window.addEventListener("resize", handleResize);

        handleResize();

        return () => {
            window.removeEventListener("resize", handleResize);
        };
    }, []);
    return maxElements;
}