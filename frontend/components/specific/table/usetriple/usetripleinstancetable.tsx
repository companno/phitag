// services
import { useFetchInstances, useFetchPagedUseTripleInstance } from "../../../../lib/service/instance/InstanceResource";

// models
import Phase from "../../../../lib/model/phase/model/Phase";
import Usage from "../../../../lib/model/phitagdata/usage/model/Usage";
import UseTripleInstance, { UseTripleInstanceConstructor } from "../../../../lib/model/instance/usetripleinstance/model/UseTripleInstance";

// components
import LoadingComponent from "../../../generic/loadingcomponent";
import AddInstanceToPhaseModal from "../../modal/addinstancetophasemodal";
import { useEffect, useState } from "react";
import PageChange from "../../../generic/table/pagination";
import { data } from "autoprefixer";
import GenerateInstancesForPhaseModal from "../../modal/generateinstancesforphasemodal";

const UseTripleInstanceTable: React.FC<{ phase: Phase, modalState: { openData: boolean, callbackData: Function, openGenerate: boolean, callbackGenerate: Function } }> = ({ phase, modalState }) => {

    const [page, setPage] = useState(0);
    const usetripleinstances = useFetchPagedUseTripleInstance(phase?.getId().getOwner(), phase?.getId().getProject(), phase?.getId().getPhase(), page, !!phase);

    // Reload the data on reload
    useEffect(() => {
        usetripleinstances.mutate();
    }, [phase]);

    if (!phase || usetripleinstances.isLoading || usetripleinstances.isError) {
        return <LoadingComponent />;
    }

    return (
        <div>

            <div className="flex flex-col font-dm-mono-medium">
                <div className="overflow-auto">
                    <table className="min-w-full border-b-[1px] border-base16-gray-200 divide-y divide-base16-gray-200">
                        <thead className="font-bold text-lg">
                            <tr>
                                <th scope="col"
                                    className="px-6 py-3 text-left uppercase tracking-wider whitespace-nowrap">
                                    Instance ID
                                </th>

                                <th scope="col"
                                    className="px-6 py-3 text-left uppercase tracking-wider whitespace-nowrap">
                                    First Usage
                                </th>

                                <th scope="col"
                                    className="px-6 py-3 text-left uppercase tracking-wider whitespace-nowrap">
                                    Second Usage
                                </th>

                                <th scope="col"
                                    className="px-6 py-3 text-left uppercase tracking-wider whitespace-nowrap">
                                    Third Usage
                                </th>

                                <th scope="col"
                                    className="px-6 py-3 text-left uppercase tracking-wider whitespace-nowrap">
                                    Label Set
                                </th>

                                <th scope="col"
                                    className="px-6 py-3 text-left uppercase tracking-wider whitespace-nowrap">
                                    Non Label
                                </th>
                            </tr>
                        </thead>
                        <tbody className=" text-base16-gray-700">
                            {usetripleinstances.data.getContent().map((instance, i) => {
                                //@ts-ignore, TODO: fix this
                                let usetripleinstance: UseTripleInstance = instance;
                                return (<tr key={usetripleinstance.getId().getInstanceId()}>

                                    <td className="px-6 py-4 whitespace-nowrap">
                                        {usetripleinstance.getId().getInstanceId()}
                                    </td>

                                    <td className="px-6 py-4  font-dm-mono-light">
                                        <span key={i} className="tooltip group w-fit">
                                            {getFormatedUsage(usetripleinstance.getFirstusage())}
                                            <div className="tooltip-container group-hover:scale-100">
                                                <div className="whitespace-nowrap mx-4 my-2">
                                                    Data ID: {usetripleinstance.getFirstusage().getId().getDataid()}
                                                </div>
                                            </div>
                                        </span>
                                    </td>

                                    <td className="px-6 py-4  font-dm-mono-light">
                                        <span key={i} className="tooltip group w-fit">
                                            {getFormatedUsage(usetripleinstance.getSecondusage())}
                                            <div className="tooltip-container group-hover:scale-100">
                                                <div className="whitespace-nowrap mx-4 my-2">
                                                    Data ID: {usetripleinstance.getSecondusage().getId().getDataid()}
                                                </div>
                                            </div>
                                        </span>
                                    </td>


                                    <td className="px-6 py-4  font-dm-mono-light">
                                        <span key={i} className="tooltip group w-fit">
                                            {getFormatedUsage(usetripleinstance.getThirdusage())}
                                            <div className="tooltip-container group-hover:scale-100">
                                                <div className="whitespace-nowrap mx-4 my-2">
                                                    Data ID: {usetripleinstance.getThirdusage().getId().getDataid()}
                                                </div>
                                            </div>
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        {usetripleinstance.getLabelSet().join(', ')}
                                    </td>

                                    <td className="px-6 py-4 whitespace-nowrap">
                                        {usetripleinstance.getNonLabel()}
                                    </td>
                                </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            </div>
            
            <PageChange page={page} maxPage={usetripleinstances.data.getTotalPages()} pageChangeCallback={(p: number) => {setPage(p)}} />

            <AddInstanceToPhaseModal isOpen={modalState.openData} closeModalCallback={modalState.callbackData} phase={phase} mutateCallback={usetripleinstances.mutate} />
            <GenerateInstancesForPhaseModal isOpen={modalState.openGenerate} closeModalCallback={modalState.callbackGenerate} phase={phase} mutateCallback={usetripleinstances.mutate} additional={false} additionalFileName="" />
        </div>
    );

}

export default UseTripleInstanceTable;

function getFormatedUsage(usage: Usage) {
    return (
        <div className="">
            {usageContextBuilder(usage).map((sentence, index) => {
                return (
                    <span
                        key={index}
                        className={sentence.highlight === "bold" ? "font-dm-mono-medium" : sentence.highlight === "color" ? "inline font-dm-sans-bold text-lg text-base16-green" : ""}>
                        {sentence.sentence}
                    </span>
                );
            })}
        </div>
    )
}

/**
 * Constructs a context array with Triples of the form {sentence: string, highlight: "none" | "bold" | "color"}
 * 
 * @param usage usage to construct the context from
 */
function usageContextBuilder(usage: Usage): { sentence: string, highlight: "none" | "bold" | "color" }[] {

    const context = usage.getContext();

    const contextArray: { sentence: string, highlight: "none" | "bold" | "color" }[] = [];

    // Add the first sentence
    contextArray.push({
        sentence: context.substring(0, usage.getIndexTargetSentenceStart()),
        highlight: "none"
    });

    let lastTargetTokenEnd = 0;

    usage.getIndexTargetSentence().forEach((sentence, index) => {
        lastTargetTokenEnd = sentence.left;
        usage.getIndexTargetToken().forEach((token, index) => {
            if (token.left >= sentence.left && token.right <= sentence.right) {
                // Add the sentence till the target token
                contextArray.push({
                    sentence: context.substring(lastTargetTokenEnd, token.left),
                    highlight: "bold"
                });
                // Add the target token
                contextArray.push({
                    sentence: context.substring(token.left, token.right),
                    highlight: "color"
                });
                lastTargetTokenEnd = token.right;
            }
        });
        // Add the sentence after the target token
        contextArray.push({
            sentence: context.substring(lastTargetTokenEnd, sentence.right),
            highlight: "bold"
        });
    });

    // Add the last sentence
    contextArray.push({
        sentence: context.substring(usage.getIndexTargetSentenceEnd()),
        highlight: "none"
    });

    return contextArray;

}
