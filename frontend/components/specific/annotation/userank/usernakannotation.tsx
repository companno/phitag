// react
import { useEffect, useState } from "react";

// icon
import { FiBookmark, FiFeather } from "react-icons/fi";

// Serrvice
import { fetchRandomInstance, useFetchPagedUseRankInstance } from "../../../../lib/service/instance/InstanceResource";
import useStorage from "../../../../lib/hook/useStorage";

// model
import Phase from "../../../../lib/model/phase/model/Phase";
import UserankInstance, { UseRankInstanceConstructor } from "../../../../lib/model/instance/userankinstance/model/UseRankInstance";
import { useFetchAnnotationAccess } from "../../../../lib/service/phase/PhaseResource";
import UsageField from "../usage/usagefield";
import { toast } from "react-toastify";
import Router from "next/router";
import AddUseRankJudgementCommand from "../../../../lib/model/judgement/userankjudgement/command/AddUseRankJudgementCommand";
import { annotateUserank, useFetchPagedUseRankJudgements } from "../../../../lib/service/judgement/JudgementResource";
import LoadingComponent from "../../../generic/loadingcomponent";
import ProgressBar from "../progressbar/progressbar";
import React from "react";
import Draggable from "../usage/userank/dragabbleusage";




const UseRankAnnotation: React.FC<{ phase: Phase }> = ({ phase }) => {


    // States
    const [annotation, setAnnotation] = useState({
        instance: null as unknown as UserankInstance,
        comment: "",

        initialLoad: true

    });

   

    const page: number = 0;
    const userankinstances = useFetchPagedUseRankInstance(phase?.getId().getOwner(), phase?.getId().getProject(), phase?.getId().getPhase(), page, !!phase);

    const { data: userankjudgementsData, mutate: mutateUserankJudgements } = useFetchPagedUseRankJudgements(
        phase?.getId().getOwner(),
        phase?.getId().getProject(),
        phase?.getId().getPhase(),
        page,
        !!phase
    );
    

    const labelArray = annotation.instance ? annotation.instance.getLabelSet() : null;
    const noLabel = annotation.instance ? annotation.instance.getNonLabel() : null;

    const rank = {
        first: labelArray ? labelArray[0]: "First Usage",
        second: labelArray ? labelArray[1]: "Second Usage",
        third: labelArray ? labelArray[2]: "Third Usage",
        fourth: labelArray ? labelArray[3]: "Fourth Usage",
    }

    const nonLabelSet = {
        nonlabel: noLabel ? noLabel[0] : "-"
    }
    const [judgementData, setJudgementData] = useState("");

    const handleUsagesReordered = (rank: string) => {
        setJudgementData(rank);
    };


    // Hooks
    const storage = useStorage();
    const annotationAccess = useFetchAnnotationAccess(phase?.getId().getOwner(), phase?.getId().getProject(), phase?.getId().getPhase(), !!phase);

    

    const handleSubmitAnnotation = () => {
        mutateUserankJudgements();
        const judgement: string = judgementData;
        console.log(judgement, "judgement")
        if(!judgement){
            toast.info("Please rank first or choose non label")
            return;
        }

        if (userankinstances.data.getTotalElements() === userankjudgementsData.getTotalElements()) {
            toast.success("Congrats!!! You finished it all");
            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getName()}/done`);
            return
        }
        if (userankinstances.data.getTotalElements() !== userankjudgementsData.getTotalElements()) {
            const resultCommand = verifyResultCommand(phase, judgement, annotation);
            setAnnotation({
                ...annotation,
                instance: null as unknown as UserankInstance,
                comment: ""
            });
            if (resultCommand !== null) {
                annotateUserank(resultCommand, storage.get)
                    .then((result) => {
                        fetchNewAnnotation();
                    }).catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error("Error while adding judgement: " + error.response.data.message + "!");
                        } else {
                            toast.warning("The system is currently not available, please try again later!");
                        }
                    }
                    );
            }
            setJudgementData("");
        }


    }

    const handleSkip = () => {

        const judgement: string = annotation.instance.getNonLabel().toString();


        mutateUserankJudgements();
        if (userankinstances.data.getTotalElements() === userankjudgementsData.getTotalElements()) {
            toast.success("Congrats!!! You finished it all");
            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getName()}/done`);
            return
        }
        if (userankinstances.data.getTotalElements() !== userankjudgementsData.getTotalElements()) {
            const resultCommand = verifyResultCommand(phase, judgement, annotation);
            setAnnotation({
                ...annotation,
                instance: null as unknown as UserankInstance,
                comment: ""
            });
            if (resultCommand !== null) {
                annotateUserank(resultCommand, storage.get)
                    .then((result) => {
                        fetchNewAnnotation();
                    }).catch((error) => {
                        if (error?.response?.status === 500) {
                            toast.error("Error while adding judgement: " + error.response.data.message + "!");
                        } else {
                            toast.warning("The system is currently not available, please try again later!");
                        }
                    }
                    );
            }
        }


    }

    const fetchNewAnnotation = () => {
        mutateUserankJudgements();
        if (userankinstances.data.getTotalElements() === userankjudgementsData.getTotalElements()) {
            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getName()}/done`);
        }
        else {
            fetchRandomInstance<UserankInstance, UseRankInstanceConstructor>(phase.getId().getOwner(), phase.getId().getProject(), phase.getId().getPhase(), (new UseRankInstanceConstructor()), storage.get)
                .then((instance) => {
                    if (instance && userankinstances.data.getTotalElements() !== userankjudgementsData.getTotalElements()) {
                        setAnnotation({
                            ...annotation,

                            instance: instance,
                            comment: "",
                        });
                    } else {
                        toast.info("Could not fetch new annotation. Check if instances are provided for annotation.");
                    }

                })
                .catch((error) => {
                    toast.error("Could not fetch new annotation. Check if instances are provided for annotation.");
                    Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
                });

        }

        fetchRandomInstance<UserankInstance, UseRankInstanceConstructor>(phase.getId().getOwner(), phase.getId().getProject(), phase.getId().getPhase(), (new UseRankInstanceConstructor()), storage.get)
            .then((instance) => {
                if (instance && userankinstances.data.getTotalElements() !== userankjudgementsData.getTotalElements()) {
                    setAnnotation({
                        ...annotation,

                        instance: instance,
                        comment: "",
                    });
                } else if (!instance && userankinstances.data.getTotalElements() !== userankjudgementsData.getTotalElements()) {

                    toast.info("Could not fetch new annotation. Check if instances are provided for annotation.");
                }

            })
            .catch((error) => {
                toast.error("Could not fetch new annotation. Check if instances are provided for annotation.");
                Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
            });
    }

    // Hook

    useEffect(() => {
        if (userankinstances.data.getTotalElements()) {

            if (userankinstances.data.getTotalElements() === userankjudgementsData.getTotalElements()) {
                toast.info("No instance available to anotate")
                Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getName()}/done`);


            }

            if (userankinstances.data.getTotalElements() !== userankjudgementsData.getTotalElements()) {
                mutateUserankJudgements();
                if (annotationAccess.isError) {
                    Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
                    return;
                } else if (annotation.initialLoad) {
                    fetchRandomInstance<UserankInstance, UseRankInstanceConstructor>(
                        phase.getId().getOwner(),
                        phase.getId().getProject(),
                        phase.getId().getPhase(),
                        new UseRankInstanceConstructor(),
                        storage.get
                    )
                        .then((instance) => {
                            if (instance) {
                                setAnnotation((prevAnnotation) => ({
                                    ...prevAnnotation,
                                    instance: instance,
                                    comment: "",
                                    initialLoad: false,
                                }));
                            } /* else {
                                toast.info("You finished all");
                                Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getName()}/done`);
                            } */
                        })
                        .catch((error) => {
                            toast.error("Could not fetch new annotation. Check if instances are provided for annotation.");
                            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
                        });
                }
            }

        }



    }, [userankinstances.data.getTotalElements(), userankjudgementsData.getTotalElements(), annotationAccess, annotation.initialLoad, storage, phase]);


    if (!phase || !annotation.instance || annotation.initialLoad) {
        return <LoadingComponent />;
    }

    return (

        <div className="w-full flex flex-col justify-between">
            <ProgressBar currentValue={userankjudgementsData.getTotalElements()} minValue={0} maxValue={userankinstances.data.getTotalElements()} />
            {(phase.getTaskHead() ?? "") !== "" && (
                <div className="w-half shadow-md ">
                    <div className="m-8 flex flex-row">
                        <div className="my-4">
                            <FiBookmark className="basic-svg" />
                        </div>
                        <div className="border-r-2 mx-4" />
                        <div className="my-4 font-dm-mono-light text-lg overflow-auto">
                            {phase.getTaskHead()}
                        </div>

                    </div>
                </div>
            )}
                <Draggable  usages={[
                {
                    key: rank.first,
                    usage: annotation.instance.getFirstusage(),
                },
                {
                    key: rank.second,
                    usage: annotation.instance.getSecondusage(),
                },
                {
                    key: rank.third,
                    usage: annotation.instance.getThirdusage(),
                },
                {
                    key: rank.fourth,
                    usage: annotation.instance.getFourthusage(),
                },
            ]} handleUsagesReordered={handleUsagesReordered } />


            <div className="w-full flex flex-col justify-center space-y-4 ">

            </div>

            <div className="w-full flex flex-row my-8 items-center justify-between xl:justify-center xl:space-x-6">
                <div
                    className="flex shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200 font-dm-mono-medium"
                    onClick={handleSubmitAnnotation}>
                    <div className="w-10 h-8 m-6 text-center text-lg">
                        Save
                    </div>
                </div>
                <div
                    className="flex shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200 font-dm-mono-medium"
                    onClick={handleSkip}>
                    <div className="w-10 h-8 m-6 text-center text-lg">
                        {nonLabelSet.nonlabel}
                    </div>
                </div>
            </div>
            <div className="w-full flex flex-col self-center items-left font-dm-mono-regular text-lg">
                <div className="font-bold text-lg">
                    Comment
                </div>
                <div className="h-32 flex items-start border-l-2 py-2 px-3 mt-2">
                    <FiFeather className='basic-svg' />
                    <textarea
                        className="w-full h-full resize-none pl-3 flex flex-auto outline-none border-none"
                        name="description"
                        placeholder={"Comment"}
                        value={annotation.comment}
                        onChange={(e: any) => setAnnotation({
                            ...annotation,
                            comment: e.target.value
                        })} />
                </div>
            </div>
        </div>
    );


};

export default UseRankAnnotation;

function verifyResultCommand(phase: Phase, judgement: string, annotation: {
    instance: UserankInstance;

    comment: string;
    initialLoad: boolean;
}): AddUseRankJudgementCommand | null {
    if (phase === undefined || phase === null) {
        toast.warning("This should not happen. Please contact the administrator.");
        return null;
    }

    if (annotation.instance === undefined || annotation.instance === null) {
        toast.warning("This should not happen. Please contact the administrator.");
        return null;
    }

    if (judgement === undefined || judgement === null || judgement === "") {
        toast.error("Please select a judgement.");
        return null;
    }

    return new AddUseRankJudgementCommand(
        phase.getId().getOwner(),
        phase.getId().getProject(),
        phase.getId().getPhase(),
        annotation.instance.getId().getInstanceId(),
        judgement,
        annotation.comment
    );
}