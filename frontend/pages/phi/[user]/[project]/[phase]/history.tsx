// Next
import { NextPage } from "next";
import Head from "next/head";
import Router, { useRouter } from "next/router";

// React
import { useEffect } from "react";

// Toast
import { toast } from "react-toastify";

// React Icons

// Services
import useAuthenticated from "../../../../../lib/hook/useAuthenticated";
import { useFetchPhase } from "../../../../../lib/service/phase/PhaseResource";

// models
import ANNOTATIONTYPES from "../../../../../lib/AnnotationTypes";

// components 
import FullLoadingPage from "../../../../../components/pages/fullloadingpage";
import PhaseTabBar from "../../../../../components/specific/tab/phasedatatab";

// layout
import Layout from "../../../../../components/generic/layout/layout";
import SingleContentLayout from "../../../../../components/generic/layout/singlecontentlayout";
import { useFetchSelfEntitlement } from "../../../../../lib/service/annotator/AnnotatorResource";
import UsePairJudgementHistoryTable from "../../../../../components/specific/table/usepair/usepairjudgementhistorytable";
import UseRankJudgementHistoryTable from "../../../../../components/specific/table/userank/userankjudgementhistorytable";

import WSSIMJudgementHistoryTable from "../../../../../components/specific/table/wssim/wssimjudgementhistorytable";
import LinkHead from "../../../../../components/generic/linker/linkhead";
import { FiLayers } from "react-icons/fi";
import LexSubJudgementHistoryTable from "../../../../../components/specific/table/lexsub/lexsubjudgementhistorytable";


const AnnotationHistory: NextPage = () => {

    // data
    const authenticated = useAuthenticated();

    const router = useRouter();
    const { user: username, project: projectname, phase: phasename } = router.query as { user: string, project: string, phase: string };

    // TODO: Find a better way to do this, generally
    const phase = useFetchPhase(username, projectname, phasename, router.isReady);
    const entitlement = useFetchSelfEntitlement(username, projectname, router.isReady);

    // hooks
    useEffect(() => {
        if (phase.isError || entitlement.isError) {
            toast.error("Phase not found");
            Router.push(`/phi/${username}/${projectname}`);
        }


        if (authenticated.isReady && !authenticated.isAuthenticated) {
            toast.info("Session expired, please login again.");
            Router.push("/");
        }
    }, [authenticated, phase.isError, entitlement.isError, username, projectname]);


    if (phase.isLoading || phase.phase === null || entitlement.isLoading || entitlement.entitlement === null || !router.isReady) {
        return <FullLoadingPage headtitle="History Data" />
    }

    if (phase.phase.isTutorial()) {
        toast.error("Tutorial phases do not have annotation history");
        Router.push(`/phi/${username}/${projectname}`);
        return <FullLoadingPage headtitle="History" />
    }

    if (phase.phase.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_USEPAIR) {

        return (
            <Layout>

                <Head>
                    <title>PhiTag : {phase.phase.getName()} : History </title>
                </Head>


                <SingleContentLayout>
                    <LinkHead icon={<FiLayers className="stroke-2" />}
                        links={[
                            {
                                href: `/phi/${username}`,
                                name: username,
                            },
                            {
                                href: `/phi/${username}/${projectname}`,
                                name: projectname,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}`,
                                name: phasename,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}/history`,
                                name: "History",
                            }
                        ]}
                    />

                    <div className="w-full flex flex-col 2xl:flex-row justify-between">
                        <PhaseTabBar />
                        <div />
                    </div>

                    <div className="m-8">
                        {/* @ts-ignore */}
                        <UsePairJudgementHistoryTable phase={phase.phase} />
                    </div>

                </SingleContentLayout>
            </Layout>

        );
    }

    if (phase.phase.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_USERANK) {

        return (
            <Layout>

                <Head>
                    <title>PhiTag : {phase.phase.getName()} : History </title>
                </Head>


                <SingleContentLayout>
                    <LinkHead icon={<FiLayers className="stroke-2" />}
                        links={[
                            {
                                href: `/phi/${username}`,
                                name: username,
                            },
                            {
                                href: `/phi/${username}/${projectname}`,
                                name: projectname,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}`,
                                name: phasename,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}/history`,
                                name: "History",
                            }
                        ]}
                    />

                    <div className="w-full flex flex-col 2xl:flex-row justify-between">
                        <PhaseTabBar />
                        <div />
                    </div>

                    <div className="m-8 overflow-auto">
                        {/* @ts-ignore */}
                        <UseRankJudgementHistoryTable phase={phase.phase} />
                    </div>

                </SingleContentLayout>
            </Layout>

        );
    }


    if (phase.phase.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_WSSIM) {

        return (
            <Layout>

                <Head>

                    <title>PhiTag : {phase.phase.getName()} : History </title>
                </Head>


                <SingleContentLayout>
                    <LinkHead icon={<FiLayers className="stroke-2" />}
                        links={[
                            {
                                href: `/phi/${username}`,
                                name: username,
                            },
                            {
                                href: `/phi/${username}/${projectname}`,
                                name: projectname,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}`,
                                name: phasename,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}/history`,
                                name: "History",
                            }
                        ]}
                    />


                    <div className="w-full flex flex-col 2xl:flex-row justify-between">
                        <PhaseTabBar />
                        <div />
                    </div>

                    <div className="m-8">
                        {/* @ts-ignore */}
                        <WSSIMJudgementHistoryTable phase={phase.phase} />
                    </div>

                </SingleContentLayout>
            </Layout>

        );
    } 

    if (phase.phase.getAnnotationType().getName() === ANNOTATIONTYPES.ANNOTATIONTYPE_LEXSUB) {
        return (
            <Layout>

                <Head>

                    <title>PhiTag : {phase.phase.getName()} : History </title>
                </Head>


                <SingleContentLayout>
                    <LinkHead icon={<FiLayers className="stroke-2" />}
                        links={[
                            {
                                href: `/phi/${username}`,
                                name: username,
                            },
                            {
                                href: `/phi/${username}/${projectname}`,
                                name: projectname,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}`,
                                name: phasename,
                            },
                            {
                                href: `/phi/${username}/${projectname}/${phasename}/history`,
                                name: "History",
                            }
                        ]}
                    />


                    <div className="w-full flex flex-col 2xl:flex-row justify-between">
                        <PhaseTabBar />
                        <div />
                    </div>

                    <div className="m-8">
                        {/* @ts-ignore */}
                        <LexSubJudgementHistoryTable phase={phase.phase} />
                    </div>

                </SingleContentLayout>
            </Layout>
        );
    }

    return (
        <FullLoadingPage headtitle="Annotate" />
    );
}

export default AnnotationHistory;