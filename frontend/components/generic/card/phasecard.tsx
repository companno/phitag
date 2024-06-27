// Next
import Link from "next/link";
import { useState } from "react";
// Next Components

// hooks
import useStorage from "../../../lib/hook/useStorage";

// React Icons
import { FiEdit3, FiLayers, FiPlay, FiStopCircle } from "react-icons/fi";

// Models
import Phase from "../../../lib/model/phase/model/Phase";
import ENTITLEMENTS from "../../../lib/model/entitlement/Entitlements";

// Custom Libraries
import IconButtonOnClick from "../../generic/button/iconbuttononclick";
import IconButtonWithTooltip from "../../generic/button/iconbuttonwithtooltip";
import { closePhase } from "../../../lib/service/phase/PhaseResource";
import PhaseStatusEnum from "../../../lib/model/phase/data/PhaseStatusEnum";

interface IPhaseCard {
    phase: Phase;
    
    onClickRequirements: () => void;
    onClickComputation: () => void;

    refreshCallback: () => void;

    entitlement: string;
}

const PhaseCard: React.FC<IPhaseCard> = ({ phase, onClickRequirements, onClickComputation, refreshCallback, entitlement = "NONE" }) => {

    const storage = useStorage();

    const urlprefix = `/phi/${phase.getId().getOwner()}/${phase.getId().getProject()}/${phase.getId().getPhase()}`

    const onClickClosePhase = () => {
        closePhase(phase.getId().getOwner(), phase.getId().getProject(), phase.getId().getPhase(), storage.get)
            .then(() => {
                refreshCallback();
            }).catch((error) => {
                console.log(error);
            });

    }

    if (!phase) {
        return <div />;
    }

    return (
        <Link href={`${urlprefix}/`}>
            <div className="flex flex-col h-auto w-80 shadow-md cursor-pointer hover:scale-[1.025] hover:transition-all duration-200 font-dm-mono-medium py-16 px-8">
                <div className="font-bold text-2xl ">
                    {phase.isTutorial() ? "Tutorial" : "Phase"}: {phase.getDisplayname()}
                </div>

                {/* <div className="my-8"> */}
                <div className="flex flex-row justify-between items-center my-1">
                    <div className="text-sm text-left">
                        Annotation Type:
                    </div>
                    <div className="ml-8 font-dm-mono-regular text-base16-gray-600 text-sm text-right">
                        {phase.getAnnotationType().getVisiblename()}
                    </div>
                </div>

                <div className="flex flex-row justify-between items-center mb-1">
                    <div className="text-sm text-left">
                        Sampling Strategy:
                    </div>
                    <div className="ml-8 font-dm-mono-regular text-base16-gray-600 text-sm text-right">
                        {phase.getSampling().getVisiblename()}
                    </div>
                </div>

                <div className="flex flex-row justify-between items-center mb-1">
                    <div className="text-sm text-left">
                        Status:
                    </div>
                    <div className="ml-8 font-dm-mono-regular text-base16-gray-600 text-sm text-right">
                        {phase.getStatus()}
                    </div>
                </div>
                {/* </div> */}

                <div className="mx-16 my-4 border-b-2" />

                <div className="flex flex-col mb-auto">
                    <div className="text-base">
                        Description:
                    </div>
                    <div className="mt-2 font-dm-mono-regular text-base16-gray-600 text-sm break-words ">
                        {phase.getDescription().length === 0 ? "No description provided." : phase.getDescription()}
                    </div>
                </div>

                {phase.getTutorialrequirements().length == 0 ?
                    (<div />)
                    :
                    (
                        <div>
                            <div className="mx-16 my-4 border-b-2" />

                            <div className="flex flex-col justify-between items-center mt-8">
                                <div className="flex flex-row">
                                    Tutorials:
                                </div>

                                {phase.getTutorialrequirements().length == 0 ? (
                                    <div />
                                ) : phase.getTutorialrequirements().map((tutorial, index) => {
                                    return (
                                        <div className="w-full flex flex-row justify-between items-center" key={index}>
                                            <div className="font-dm-mono-regular text-base mr-auto">
                                                {tutorial.left}:
                                            </div>
                                            <div className="flex flex-row">
                                                <div className="ml-8 font-dm-mono-light text-base16-gray-600 text-sm">
                                                    {tutorial.right ? "Completed" : "Not Passed"}
                                                </div>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>
                    )
                }

                <div className="mx-16 my-4 border-b-2" />

                <div className="flex flex-row justify-end align-bottom space-x-4" onClick={(e: any) => e.stopPropagation()}>
                    <IconButtonOnClick icon={<FiStopCircle className="basic-svg" />} onClick={onClickClosePhase} tooltip="Close Phase" hide={phase.getStatus() === PhaseStatusEnum.CLOSED || phase.isTutorial() || entitlement !== ENTITLEMENTS.ADMIN} />
                    <IconButtonOnClick icon={<FiLayers className="basic-svg" />} onClick={onClickRequirements} tooltip="Add Requirements" hide={phase.getStatus() === PhaseStatusEnum.CLOSED || entitlement !== ENTITLEMENTS.ADMIN || phase.isTutorial()} />
                    <IconButtonWithTooltip icon={<FiEdit3 className="basic-svg" />} reference={`${urlprefix}/${phase.isTutorial() ? "tutorial" : "annotate"}`} tooltip="Annotate" hide={phase.getStatus() === PhaseStatusEnum.CLOSED || entitlement !== ENTITLEMENTS.ADMIN && entitlement !== ENTITLEMENTS.USER} />
                    <IconButtonOnClick icon={<FiPlay className="basic-svg" />} onClick={onClickComputation} tooltip="Start Computational Annotation" hide={phase.getStatus() === PhaseStatusEnum.CLOSED || phase.isTutorial() || entitlement !== ENTITLEMENTS.ADMIN} />
                    {/* <IconButtonWithTooltip icon={<FiGitPullRequest className="basic-svg" />} reference={`${urlprefix}/history`} tooltip="View Personal History" hide={entitlement !== ENTITLEMENTS.ADMIN && entitlement !== ENTITLEMENTS.USER || phase.isTutorial()} />
                    <IconButtonWithTooltip icon={<FiTrendingUp className="basic-svg" />} reference={`${urlprefix}/statistic`} tooltip="View Statistic of Phase" hide={phase.isTutorial()} />
                    <IconButtonWithTooltip icon={<FiDatabase className="basic-svg" />} reference={`${urlprefix}/instance`} tooltip="View Data" hide={phase.isTutorial() && entitlement !== ENTITLEMENTS.ADMIN} /> */}
                </div>
            </div>
        </Link>

    );

}

export default PhaseCard;