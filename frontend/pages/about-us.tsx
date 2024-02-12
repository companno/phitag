import Head from "next/head";
import path from "path";

// Data
import { getSortedDirectoryData } from "../lib/hook/useData";

// Custom Components
import BasicLayout from "../components/generic/layout/basiclayout";
import ContentBuilder from "../components/generic/contentbox/contentbuilder";
import ContentLayout from "../components/generic/layout/contentlayout";

export default function AboutUs({ teamData }: { aboutusData: Array<any>, teamData: Array<any> }) {

    const contentBuilderFunction = (data: any, index: number) => {
        return (
            <ContentBuilder key={index} data={data} />
        )
    }

    return (
        <BasicLayout>
            <Head>
                <title>PhiTag: About Us</title>
            </Head>

            <ContentLayout>
                <div className="flex flex-col">

                    <div className="mt-16 portrait:text-center font-uni-corporate-bold font-bold text-base16-gray-900 text-4xl">
                        The Team
                    </div>

                    {teamData.map(contentBuilderFunction)}

                </div>
            </ContentLayout>
        </BasicLayout>
    );

}

export async function getStaticProps() {

    const teamDirecotry = path.join(process.cwd(), 'data/about-us/team');
    const teamData: Array<any> = getSortedDirectoryData(teamDirecotry);

    return {
        props: {
            teamData: JSON.parse(JSON.stringify(teamData)),
        },
    };
}