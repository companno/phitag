// Next
import { NextPage } from "next";
import Router from "next/router";

// React
import { useEffect, useState } from "react";

// Toast
import { toast } from "react-toastify";

// Hooks
import useAuthenticated from "../../lib/hook/useAuthenticated";

// Custom Controllers

// Custom Components
import Layout from "../../components/generic/layout/layout";
import Head from "next/head";
import ContentLayout from "../../components/generic/layout/contentlayout";
import { FiSearch } from "react-icons/fi";
import GuideHeader from "../../lib/model/guides/GuideHeader";
import { getSortedGuideHeader } from "../../lib/hook/useGuide";
import GuideCard from "../../components/generic/card/guidecard";


export function Guides({ allGuides }: { allGuides: GuideHeader[] }) {
    // State and Data
    const [searchField, setSearchField] = useState({
        fieldQuery: "",
    });

    function queryGuides(guides: GuideHeader[]) {
        return guides.filter((guide) => {
            return guide.title.toLowerCase().includes(searchField.fieldQuery.toLowerCase());
        });
    }



    // auth
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
                <title>PhiTag: User Overview</title>
            </Head>

            <ContentLayout>
                <div className='flex flex-col w-full'>

                    <div className="flex flex-col md:flex-row md:items-center md:space-x-6">


                        <div className="flex font-dm-mono-medium font-bold text-2xl">
                            Guides
                        </div>

                        <div className="flex flex-row w-full ">

                            <div className="flex flex-row w-full basis-full items-center border-b-2 py-2 px-3 my-4">
                                <input
                                    className="pr-3 flex flex-auto outline-none border-none font-dm-sans-medium font-bold"
                                    type={"text"}
                                    placeholder="Search Term"
                                    value={searchField.fieldQuery}
                                    onChange={(e) => setSearchField({
                                        ...searchField,
                                        fieldQuery: e.target.value,
                                    })} />
                                <FiSearch className='basic-svg' />
                            </div>
                        </div>
                    </div>

                    <div className="flex flex-col justify-center px-4 my-8 space-y-8">
                        {
                            queryGuides(allGuides).map((guide) => (
                                <GuideCard key={guide.id} guide={guide} />
                            ))
                        }
                    </div>
                </div>
            </ContentLayout>
        </Layout>
    )

}

export default Guides;

export async function getStaticProps() {
    const allGuides: GuideHeader[] = getSortedGuideHeader();
    return {
        props: {
            allGuides: JSON.parse(JSON.stringify(allGuides)),
        },
    };
}