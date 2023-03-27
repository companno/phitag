//React Modules
import React, { FC } from "react";

//Next Modules
import Link from 'next/link'
import { useRouter } from "next/router";

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
        <div className="my-2 mx-4 flex flex-col 2xl:flex-row justify-start space-y-2 2xl:space-x-10 2xl:space-y-0">
            <Tab href={`${urlprefix}`} title="Overview" isSelected={isSelectedOverview} />
            <Tab href={`${urlprefix}/data`} title="Data" isSelected={isSelectedData} />
            <Tab href={`${urlprefix}/annotator`} title="Annotator" isSelected={isSelectedAnnotator} />
            <Tab href={`${urlprefix}/task`} title="Tasks" isSelected={isSelectedTask} />
            <Tab href={`${urlprefix}/statistic`} title="Statistic" isSelected={isSelectedStatistic} />
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
