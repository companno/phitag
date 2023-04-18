import { useEffect, useState } from "react";
import LexSubInstance, { LexSubInstanceConstructor } from "../../../../lib/model/instance/lexsubinstance/model/LexSubInstance";
import Phase from "../../../../lib/model/phase/model/Phase";
import useStorage from "../../../../lib/hook/useStorage";
import { useFetchAnnotationAccess } from "../../../../lib/service/phase/PhaseResource";
import { toast } from "react-toastify";
import AddLexSubJudgementCommand from "../../../../lib/model/judgement/lexsubjudgement/command/AddLexSubJudgementCommand";
import { annotateLexSub } from "../../../../lib/service/judgement/JudgementResource";
import Router from "next/router";
import { fetchRandomInstance } from "../../../../lib/service/instance/InstanceResource";
import LoadingComponent from "../../../generic/loadingcomponent";
import UsageField from "../usage/usagefield";
import { FiArrowRight, FiChevronRight, FiEdit3, FiFeather } from "react-icons/fi";

const LexSubAnnotation: React.FC<{ phase: Phase }> = ({ phase }) => {

    const [annotation, setAnnotation] = useState({
        instance: null as unknown as LexSubInstance,

        judgement: "",
        comment: "",
        initialLoad: true

    });

    const storage = useStorage();
    const annotationAccess = useFetchAnnotationAccess(phase?.getId().getOwner(), phase?.getId().getProject(), phase?.getId().getPhase(), !!phase);

    const handleSubmitAnnotation = () => {
        if (annotation.instance === null) {
            toast.warning("This should not happen. Please try again.");
            return;
        }

        const resultCommand = new AddLexSubJudgementCommand(
            phase.getId().getOwner(),
            phase.getId().getProject(),
            phase.getId().getPhase(),
            annotation.instance.getId().getInstanceId(),

            annotation.judgement,
            annotation.comment
        );
        setAnnotation({
            ...annotation,

            instance: null as unknown as LexSubInstance,
            judgement: "",
            comment: "",
        });

        if (resultCommand !== null) {
            annotateLexSub(resultCommand, storage.get)
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
        fetchRandomInstance<LexSubInstance, LexSubInstanceConstructor>(phase.getId().getOwner(), phase.getId().getProject(), phase.getId().getPhase(), (new LexSubInstanceConstructor()), storage.get)
            .then((instance) => {
                setAnnotation({
                    ...annotation,

                    instance: instance,
                    judgement: "",
                    comment: "",
                });
            }).catch((error) => {
                toast.error("Could not fetch new annotation. Check if instances are provided for annotation.");
                Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
            });
    }


    useEffect(() => {
        if (annotationAccess.isError) {
            Router.push(`/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}`);
        }

        if (!annotationAccess.isError && annotationAccess.hasAccess && annotation.initialLoad) {
            fetchRandomInstance<LexSubInstance, LexSubInstanceConstructor>(phase.getId().getOwner(), phase.getId().getProject(), phase.getId().getPhase(), (new LexSubInstanceConstructor()), storage.get)
                .then((instance) => {
                    setAnnotation({
                        instance: instance,

                        judgement: "",
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
            <div className="w-full flex flex-col justify-center space-y-4 ">
                <UsageField usage={annotation.instance.getUsage()} />
            </div>

            <div className="w-full flex flex-col my-8 self-center items-left font-dm-mono-regular text-lg">
                <div className="font-bold text-lg">
                    Judgement
                </div>
                <div className="h-32 flex items-start border-l-2 py-2 px-3 mt-2">
                    <FiEdit3 className='basic-svg' />
                    <textarea
                        className="w-full h-full resize-none pl-3 flex flex-auto outline-none border-none"
                        name="description"
                        placeholder={"Judgement"}
                        value={annotation.judgement}
                        onChange={(e: any) => setAnnotation({
                            ...annotation,
                            judgement: e.target.value
                        })} />
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

            <div className="flex self-end">
                <button className="text-base16-gray-100 bg-base16-gray-900 hover:text-base16-green transition-all duration-500"
                    onClick={() => handleSubmitAnnotation()}>
                    <div className="flex my-2 mx-8 font-dm-mono-medium font-bold text-xl items-center">
                        Submit <FiChevronRight className='basic-svg stroke-[3]' />
                    </div>
                </button>
            </div>
        </div>
    );

}

export default LexSubAnnotation;