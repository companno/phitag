// React
import { useEffect, useState } from 'react';
import Router from 'next/router';

//Next Modules
import { NextPage } from 'next';
import Head from 'next/head';

// Toast
import { toast } from 'react-toastify';

// Icons
import { FiGrid, FiList, FiSearch } from 'react-icons/fi';

// Custom Controllers
import { useQueryProjects } from '../../../lib/service/project/ProjectResource';
import useAuthenticated from '../../../lib/hook/useAuthenticated';
import Layout from '../../../components/generic/layout/layout';
import ContentLayout from '../../../components/generic/layout/contentlayout';
import LoadingComponent from '../../../components/generic/loadingcomponent';
import ProjectCard from '../../../components/generic/card/projectcard';
import HelpButton from '../../../components/generic/button/helpbutton';

//Custom Modules

// Layout

const Projects: NextPage = () => {

    // Data & Hooks
    const authenticated = useAuthenticated();

    const [searchField, setSearchField] = useState({
        grid: false,
        fieldQuery: "",
    });

    const projects = useQueryProjects(searchField.fieldQuery);

    // effects
    useEffect(() => {
        if (authenticated.isReady && !authenticated.isAuthenticated) {
            toast.info("Session expired, please login again.");
            Router.push("/");
        }
    }, [authenticated]);


    return (
        <Layout>

            <Head>
                <title>PhiTag: Project Overview</title>
            </Head>

            <ContentLayout>
                <div className='flex flex-col w-full'>

                    <div className="flex flex-col md:flex-row md:items-center md:space-x-6">

                        <div className="flex font-dm-mono-medium font-bold text-2xl">
                            Projects
                        </div>

                        <div className="flex flex-row w-full ">

                            <div className="flex flex-row w-full basis-full items-center border-b-2 py-2 px-3 my-4">
                                <input
                                    className="pr-3 flex flex-auto outline-none border-none font-dm-sans-medium font-bold"
                                    placeholder="Search Term"
                                    type={"text"}
                                    value={searchField.fieldQuery}
                                    onChange={(e) => setSearchField({
                                        ...searchField,
                                        fieldQuery: e.target.value
                                    })} />
                                <FiSearch className='basic-svg' />
                            </div>
                            <div className="hidden xl:visible xl:flex items-center my-4 ml-4">
                                {searchField.grid ?
                                    <button className="flex flex-auto font-dm-mono-medium font-black" onClick={() => setSearchField({
                                        ...searchField,
                                        grid: false,
                                    })}>
                                        <FiGrid className='basic-svg' />
                                    </button>
                                    :
                                    <button className="flex flex-auto font-dm-mono-medium font-black" onClick={() => setSearchField({
                                        ...searchField,
                                        grid: true,
                                    })}>
                                        <FiList className='basic-svg' />
                                    </button>}
                            </div>
                            <div className="flex items-center my-4 ml-4">
                                <HelpButton
                                    title="Help: Project Pool"
                                    tooltip="Help: Project Pool"
                                    text="The project pool is a list of all projects that are currently available on PhiTag. You can search for projects by name or owner."
                                    reference="/guide/explained-pools"
                                />
                            </div>
                        </div>
                    </div>

                    {
                        projects.isLoading ? <LoadingComponent /> :
                            <div className={searchField.grid ? "project-grid" : "project-list"}>
                                {

                                    projects.projects.map((project) => (
                                        <ProjectCard key={project.getId().getOwner() + "-" + project.getId().getName()} project={project} />
                                    ))
                                }
                            </div>
                    }

                </div>
            </ContentLayout>
        </Layout>
    );
}

export default Projects;
