// React Modules
import { useEffect } from "react";

// Next Modules
import { NextPage } from "next";
import Router from 'next/router';
import Head from "next/head";

// Custom Hooks
import useAuthenticated from "../lib/hook/useAuthenticated";

// Custom Components
import DashboardCard from "../components/generic/card/dashboardcard";

// Custom Layouts
import Layout from "../components/generic/layout/layout";
import CenteredLayout from "../components/generic/layout/centeredlayout";
import { toast } from "react-toastify";

const Dashboard: NextPage = () => {

    // Hooks & Fetching
    const authenticated = useAuthenticated();

    useEffect(() => {
        if (authenticated.isReady && !authenticated.isAuthenticated) {
            toast.info("Session expired, please login again.");
            Router.push("/");
        }
    }, [authenticated]);

    return (
        <Layout>

            <Head>
                <title>PhiTag: Home</title>
            </Head>


            <CenteredLayout>
                <div className="w-full flex flex-col 3xl:w-2/3">
                    <div className="flex items-center justify-center mb-2">
                        <div className="flex font-dm-mono-medium font-bold text-xl lg:text-2xl 2xl:text-3xl">
                            Project
                        </div>
                    </div>

                    <div className="grid grid-flow-row grid-cols-2 md:grid-cols-3 gap-4">
                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Create a new Project"
                                description="Create a new Project"
                                link="/project/create" />
                        </div>
                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Public Projects"
                                description="Browse all public projects"
                                link="/pool/project" />
                        </div>

                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="My Projects"
                                description="Browse all your projects"
                                link="/pool/project/personal" />
                        </div>

                    </div>

                    <div className="flex items-center justify-center mt-8 mb-2">
                        <div className="flex font-dm-mono-medium font-bold text-xl lg:text-2xl 2xl:text-3xl">
                            Pools
                        </div>
                    </div>
                    <div className="grid grid-flow-row grid-cols-2 md:grid-cols-3 gap-4">


                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Human Annotators"
                                description="Browse the human annotator pool"
                                link="/pool/annotator" />
                        </div>

                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Computational Annotators"
                                description="Browse the computational annotator pool"
                                link="/pool/annotator/computational" />
                        </div>

                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Corpus"
                                description="Browse the corpus and create custom usages for your project"
                                link="/corpus" />
                        </div>


                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Joblisting"
                                description="Browse joblistings or create new ones"
                                link="/pool/joblisting" />
                        </div>
                    </div>

                    <div className="flex items-center justify-center mt-8 mb-2">
                        <div className="flex font-dm-mono-medium font-bold text-xl lg:text-2xl 2xl:text-3xl">
                            Guides
                        </div>
                    </div>
                    <div className="grid grid-flow-row grid-cols-2 md:grid-cols-3 gap-4">

                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Quick Start"
                                description="Read up onto how to use this tool effectively"
                                link="/guides" />
                        </div>

                        <div className="hidden sm:visible col-span-1 row-span-1 aspect-square " />

                        <div className="col-span-1 row-span-1 aspect-square">
                            <DashboardCard
                                title="Lexicography"
                                description="Read up onto how to use this tool as a lexicographer"
                                link="/guides" />
                        </div>
                    </div>
                </div>

            </CenteredLayout>

        </Layout>
    );
}

export default Dashboard;