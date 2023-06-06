//React Modules
import React, { FC } from "react";

//Next Modules
import Link from 'next/link'
import { useRouter } from "next/router";
import HelpButton from "../../generic/button/helpbutton";

interface IPropsTab {
    href: string;
    title: string;
    isSelected: boolean;
}

const ProjectTabBar: React.FC<{}> = () => {

    const router = useRouter();
    const path = router.pathname;

    const { user: username, project: projectname } = router.query as { user: string, project: string };

    const isSelectedOverview = path == "/phi/[user]/[project]";
    const isSelectedData = path == "/phi/[user]/[project]/data";
    const isSelectedAnnotator = path == "/phi/[user]/[project]/annotator";
    const isSelectedTask = path == "/phi/[user]/[project]/task";
    const isSelectedStatistic = path == "/phi/[user]/[project]/statistic";


    const urlprefix = `/phi/${username}/${projectname}`;

    return (
        <div className="w-full flex flex-col 2xl:flex-row justify-between">

            <div className="my-2 mx-4 flex flex-col 2xl:flex-row justify-start space-y-2 2xl:space-x-10 2xl:space-y-0">
                <Tab href={`${urlprefix}`} title="Overview" isSelected={isSelectedOverview} />
                <Tab href={`${urlprefix}/data`} title="Data" isSelected={isSelectedData} />
                <Tab href={`${urlprefix}/annotator`} title="Annotator" isSelected={isSelectedAnnotator} />
                <Tab href={`${urlprefix}/task`} title="Tasks" isSelected={isSelectedTask} />
                <Tab href={`${urlprefix}/statistic`} title="Statistic" isSelected={isSelectedStatistic} />
            </div>

            <div className="my-2 mx-4 self-end 2xl:self-center">

                <HelpButton
                    title="Help: Projects"
                    tooltip="Help: Projects"
                    text="
                    This is the project page. 
                    The overview tab shows you the project description, guidelines and the phases of the project.
                    Under the data tab you can add data (e.g. usages) to the project.
                    The annotator tab shows you the annotators of the project and allows you to add new (human and computational) annotators.
                    The tasks tab shows the progress of computational annotators per phase and annotator.
                    The statistic tab shows you the statistics of the project. 
                    "
                    reference="/guide/explained-project"

                />
            </div>
        </div>

    );
};

const Tab: FC<IPropsTab> = ({ href, title, isSelected }) => (

    <Link href={href}>
        <a className={isSelected ? "project-tab-selected" : "project-tab-unselected"}>
            <div className="my-2 mx-10">
                {title}
            </div>
        </a>
    </Link>
);

export default ProjectTabBar;
