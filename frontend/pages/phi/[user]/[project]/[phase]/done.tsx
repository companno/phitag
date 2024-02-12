import { useEffect } from "react";
import React from "react";
// icon
import { FiEdit3 } from "react-icons/fi";
import Layout from "../../../../../components/generic/layout/layout";
import SingleContentLayout from "../../../../../components/generic/layout/singlecontentlayout";
import LinkHead from "../../../../../components/generic/linker/linkhead";
import { useRouter } from "next/router";
import { useFetchPhase } from "../../../../../lib/service/phase/PhaseResource";

import { toast } from "react-toastify";
import { useFetchUser } from "../../../../../lib/service/user/UserResource";
import useAuthenticated from "../../../../../lib/hook/useAuthenticated";
import useStorage from "../../../../../lib/hook/useStorage";

const SubmissionsCard = () => {

    const router = useRouter();
    const { user: username, project: projectname, phase: phasename } = router.query as { user: string, project: string, phase: string };

    const phase = useFetchPhase(username, projectname, phasename, router.isReady);
    const storage  = useStorage();
    const checkUser = useFetchUser(storage.get("USER"))
    const authenticated = useAuthenticated()

    const handleSubmit = () => {

        const code = phase.phase?.getCode();
        const url = "https://app.prolific.com/submissions/complete?cc=";
        if (!code) {
            toast.error("There is an error in submitting the study. Please contact the researcher")
        }
        if (code && code.includes(url)) {

            window.location.href = code;

        } else if (code) {
            window.location.href = `${url}+${code}`
        }
    }


    const handleEdit = () => {
        const path = `/phi/${username}/${projectname}/${phasename}/judgement`;
        router.push(path);
    }
    useEffect(() => {
        /*  if (prerequisiteVisited!==username) {
            toast.info("You are not allowed to visit this page")
            router.push("/")
        } */

        if (authenticated.isReady && !authenticated.isAuthenticated) {
            toast.info("Session expired, please login again.");
            router.push("/");
        }
        //implements logic here to check all annotation is done or not 
        //implements logic here im user is prolific, if already submiited the study, restrict them to submitting again
        //implement the logic if user is phita, if already submmitted the study navigate to edit response
    }, [authenticated]);



    return (
        <Layout>

            <SingleContentLayout>

                <LinkHead icon={<FiEdit3 className="stroke-2" />}
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
                            href: "#",
                            name: "done",
                        }
                    ]}
                />
                <div className="mt-2 xl:mt-10">
                    <div className="mx-4">
                        <div className="flex flex-col items-left mt-6">
                            <div className="flex items-center justify-center my-5 font-black text-xl font-dm-mono-medium">
                                Thank you for participating in study
                            </div>
                            <div className="flex items-center justify-center my-5 font-black text-xl font-dm-mono-medium">
                                Your submission has been recorded
                            </div>

                            <div className="flex flex-row divide-x-8">
                                <button type="button" className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 text-xl font-dm-mono-medium" onClick={handleEdit}>Edit Response</button>
                                {checkUser.user?.getProlificId()!== "" || checkUser.user?.getProlificId()!== undefined || checkUser.user?.getProlificId()!== null && (
                                    <button
                                        type="button"
                                        className="block w-full mt-8 py-2 bg-base16-gray-900 text-base16-gray-100 text-xl font-dm-mono-medium"
                                        onClick={handleSubmit}
                                    >
                                        Submit
                                    </button> )}
                            </div>

                        </div>
                    </div>
                </div>
            </SingleContentLayout>
        </Layout>
    )
};

export default SubmissionsCard;
