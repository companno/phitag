// react
import { useEffect, useState } from "react";

// icon
import { FiFeather } from "react-icons/fi";

// Serrvice
import { fetchRandomInstance } from "../../../../lib/service/instance/InstanceResource";
import useStorage from "../../../../lib/hook/useStorage";

// model
import Phase from "../../../../lib/model/phase/model/Phase";
import UsePairInstance, { UsePairInstanceConstructor } from "../../../../lib/model/instance/usepairinstance/model/UsePairInstance";
import { useFetchAnnotationAccess } from "../../../../lib/service/phase/PhaseResource";
import UsageField from "../usage/usagefield";
import { toast } from "react-toastify";
import Router from "next/router";
import AddUsePairJudgementCommand from "../../../../lib/model/judgement/usepairjudgement/command/AddUsePairJudgementCommand";
import { annotateUsepair } from "../../../../lib/service/judgement/JudgementResource";
import LoadingComponent from "../../../generic/loadingcomponent";


const UsePairAnnotation: React.FC<{ phase: Phase }> = ({ phase }) => {


    // States
    const [annotation, setAnnotation] = useState({
        instance: null as unknown as UsePairInstance,
        comment: "",

        initialLoad: true

    });

    // Hooks
    const storage = useStorage();
    const annotationAccess = useFetchAnnotationAccess(phase?.getId().getOwner(), phase?.getId().getProject(), phase?.getId().getPhase(), !!phase);

    // Handlers

    const handleSubmitAnnotation = (judgement: string) => {
        if (annotation.instance === null) {
            toast.warning("This should not happen. Please try again.");
            return;
        }

        const resultCommand = verifyResultCommand(phase, judgement, annotation);
        setAnnotation({
            ...annotation,
            instance: null as unknown as UsePairInstance,
            comment: ""
        });

        if (resultCommand !== null) {
            annotateUsepair(resultCommand, storage.get)
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

    const fetchNewAnnotation = () => {
        fetchRandomInstance<UsePairInstance, UsePairInstanceConstructor>(phase.getId().getOwner(), phase.getId().getProject(), phase.getId().getPhase(), (new UsePairInstanceConstructor()), storage.get)
            .then((instance) => {
                setAnnotation({
                    ...annotation,

                    instance: instance,
                    comment: "",
                });
            }).catch((error) => {
                toast.error("Could not fetch new annotation. Check if instances are provided for annotation.");
                Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
            });
    }

    // Hook

    useEffect(() => {
        if (annotationAccess.isError) {
            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
        }

        if (!annotationAccess.isError && annotationAccess.hasAccess && annotation.initialLoad) {
            fetchRandomInstance<UsePairInstance, UsePairInstanceConstructor>(phase.getId().getOwner(), phase.getId().getProject(), phase.getId().getPhase(), (new UsePairInstanceConstructor()), storage.get)
                .then((instance) => {
                    setAnnotation({
                        instance: instance,
                        comment: "",

                        initialLoad: false
                    });
                }).catch((error) => {
                    toast.error("Could not fetch new annotation. Check if instances are provided for annotation.");
                    Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
                });
        }
    }, [annotationAccess, annotation.initialLoad, storage, phase]);

    if (!phase || !annotation.instance || annotation.initialLoad) {
        return <LoadingComponent />;
    }

    return (
        <div className="w-full flex flex-col justify-between">
            {/* randomize usages */}
            {Math.random() > 0.5 ?
                <div className="w-full flex flex-col justify-center space-y-4 ">
                    <UsageField key={0} usage={annotation.instance.getFirstusage()} />
                    <UsageField key={1} usage={annotation.instance.getSecondusage()} />
                </div>
                :
                <div className="w-full flex flex-col justify-center space-y-4 ">
                    <UsageField key={0} usage={annotation.instance.getSecondusage()} />
                    <UsageField key={1} usage={annotation.instance.getFirstusage()} />
                </div>
            }

            <div className="w-full flex flex-row my-8 items-center justify-between xl:justify-center xl:space-x-6">
                {annotation.instance.getLabelSet().concat(annotation.instance.getNonLabel()).map((label) => {
                    return (
                        <div key={label}
                            className="flex shadow-md cursor-pointer hover:bg-base16-gray-900 hover:text-base16-gray-100 transition-all duration-200 font-dm-mono-medium"
                            onClick={() => handleSubmitAnnotation(label)}>
                            <div className="w-8 h-8 m-6 text-center text-lg">
                                {label}
                            </div>
                        </div>
                    );
                })}
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

export default UsePairAnnotation;

function verifyResultCommand(phase: Phase, judgement: string, annotation: {
    instance: UsePairInstance;

    comment: string;
    initialLoad: boolean;
}): AddUsePairJudgementCommand | null {
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

    return new AddUsePairJudgementCommand(
        phase.getId().getOwner(),
        phase.getId().getProject(),
        phase.getId().getPhase(),
        annotation.instance.getId().getInstanceId(),
        judgement,
        annotation.comment
    );
}